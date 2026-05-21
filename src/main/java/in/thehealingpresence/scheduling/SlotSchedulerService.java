package in.thehealingpresence.scheduling;

import in.thehealingpresence.booking.BookingMapper;
import in.thehealingpresence.booking.domain.Booking;
import in.thehealingpresence.booking.event.BookingCreatedEvent;
import in.thehealingpresence.domain.BookingRequest;
import in.thehealingpresence.domain.BookingSource;
import in.thehealingpresence.domain.SubmissionStatus;
import in.thehealingpresence.dto.ReceptionistBookingDto;
import in.thehealingpresence.repository.BookingRequestRepository;
import in.thehealingpresence.shared.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Receptionist day-grid scheduler with cascade-blocking logic.
 *
 * <p><b>Cascade rule:</b> a 2-hour booking starting at hour T occupies {@code [T:00, T+2:00)}.
 * The slot starting at T+1 is auto-marked {@link SlotStatus#BLOCKED_BY_CASCADE} (greyed out,
 * not clickable) so the receptionist cannot try to book it.
 *
 * <p><b>Edge cases enforced by {@link #canBook}:</b>
 * <ul>
 *   <li>Lunch (1–2 PM) is a hard block — never bookable.</li>
 *   <li>2-hour booking at 12 PM is rejected (would cross lunch).</li>
 *   <li>2-hour booking at 6 PM is rejected (would exceed office close at 7 PM).</li>
 *   <li>Cancelled bookings free their slot back up (see {@link BookingRequestRepository#findOverlapping}).</li>
 * </ul>
 */
@Service
public class SlotSchedulerService {

    private final BookingRequestRepository bookingRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    @Autowired
    public SlotSchedulerService(BookingRequestRepository bookingRepository,
                                ApplicationEventPublisher eventPublisher) {
        this(bookingRepository, eventPublisher, Clock.systemDefaultZone());
    }

    /** Test-friendly ctor: inject a fixed {@link Clock} for deterministic past-slot detection. */
    public SlotSchedulerService(BookingRequestRepository bookingRepository,
                                ApplicationEventPublisher eventPublisher,
                                Clock clock) {
        this.bookingRepository = bookingRepository;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    // -------------------------------------------------------------- day-grid view --

    /**
     * Build the day-grid for {@code date}: one TimeSlot per hour from OPEN_HOUR (inclusive)
     * to CLOSE_HOUR (exclusive), with lunch and existing bookings marked.
     */
    @Transactional(readOnly = true)
    public List<TimeSlot> getDaySlots(LocalDate date) {
        LocalDateTime dayStart = date.atTime(OfficeHours.OPEN_HOUR, 0);
        LocalDateTime dayEnd = date.atTime(OfficeHours.CLOSE_HOUR, 0);

        // Pull every booking starting on this day (we use slotStart between start-of-day and end-of-day).
        List<BookingRequest> dayBookings = bookingRepository
                .findBySlotStartBetweenOrderBySlotStartAsc(dayStart, dayEnd)
                .stream()
                .filter(b -> b.getStatus() != SubmissionStatus.CANCELLED)
                .filter(b -> b.getSlotStart() != null)
                .toList();

        // Index bookings by their start hour for quick lookup.
        Map<Integer, BookingRequest> bookingByStartHour = dayBookings.stream()
                .collect(Collectors.toMap(
                        b -> b.getSlotStart().getHour(),
                        b -> b,
                        (a, b) -> a)); // shouldn't happen, but pick first on collision

        // For 2-hour bookings, also record the cascade-blocked hour.
        java.util.Set<Integer> cascadeBlockedHours = dayBookings.stream()
                .filter(b -> b.getDurationHours() != null && b.getDurationHours() == 2)
                .map(b -> b.getSlotStart().getHour() + 1)
                .collect(Collectors.toSet());

        LocalDateTime now = LocalDateTime.now(clock);
        List<TimeSlot> slots = new ArrayList<>();

        for (int hour = OfficeHours.OPEN_HOUR; hour < OfficeHours.CLOSE_HOUR; hour++) {
            LocalDateTime slotStart = date.atTime(hour, 0);
            LocalDateTime slotEnd = slotStart.plusHours(1);

            // Lunch hour wins.
            if (hour >= OfficeHours.LUNCH_START && hour < OfficeHours.LUNCH_END) {
                slots.add(new TimeSlot(slotStart, slotEnd, 0, SlotStatus.LUNCH, null));
                continue;
            }

            // Past slot.
            if (slotEnd.isBefore(now)) {
                BookingRequest pastBooking = bookingByStartHour.get(hour);
                slots.add(new TimeSlot(slotStart, slotEnd,
                        pastBooking != null && pastBooking.getDurationHours() != null
                                ? pastBooking.getDurationHours() : 0,
                        SlotStatus.PAST, pastBooking));
                continue;
            }

            BookingRequest booking = bookingByStartHour.get(hour);
            if (booking != null) {
                slots.add(new TimeSlot(slotStart, slotEnd,
                        booking.getDurationHours() != null ? booking.getDurationHours() : 1,
                        SlotStatus.BOOKED, booking));
                continue;
            }

            if (cascadeBlockedHours.contains(hour)) {
                slots.add(new TimeSlot(slotStart, slotEnd, 0, SlotStatus.BLOCKED_BY_CASCADE, null));
                continue;
            }

            slots.add(new TimeSlot(slotStart, slotEnd, 0, SlotStatus.AVAILABLE, null));
        }

        return Collections.unmodifiableList(slots);
    }

    // -------------------------------------------------------------- can-book guard --

    /**
     * @return true if a booking of {@code hours} hours starting at {@code start} is permissible
     *         (within office hours, doesn't cross lunch, doesn't overlap existing).
     */
    @Transactional(readOnly = true)
    public boolean canBook(LocalDateTime start, int hours) {
        return checkAvailability(start, hours).isEmpty();
    }

    /**
     * Returns the first {@link BookingFailure} that blocks the requested slot,
     * or empty if the slot is bookable. Used internally by {@link #canBook} and
     * {@link #tryBook(ReceptionistBookingDto)}.
     */
    @Transactional(readOnly = true)
    public Optional<BookingFailure> checkAvailability(LocalDateTime start, int hours) {
        if (hours != 1 && hours != 2) {
            return Optional.of(BookingFailure.INVALID_DURATION);
        }
        int startHour = start.getHour();

        // 1) Slot start must be a valid office hour.
        if (!OfficeHours.validStartHours().contains(startHour)) {
            return Optional.of(BookingFailure.OUTSIDE_HOURS);
        }

        // 2) The window [start, start+hours) must not overlap lunch.
        //    Standard interval-overlap: A.start < B.end && A.end > B.start.
        LocalDateTime end = start.plusHours(hours);
        LocalTime lunchStart = LocalTime.of(OfficeHours.LUNCH_START, 0);
        LocalTime lunchEnd = LocalTime.of(OfficeHours.LUNCH_END, 0);
        if (start.toLocalTime().isBefore(lunchEnd) && end.toLocalTime().isAfter(lunchStart)) {
            return Optional.of(BookingFailure.LUNCH_CROSSOVER);
        }

        // 3) The end must be at or before close.
        if (end.toLocalTime().isAfter(LocalTime.of(OfficeHours.CLOSE_HOUR, 0))
                || end.toLocalDate().isAfter(start.toLocalDate())) {
            return Optional.of(BookingFailure.CROSSES_CLOSE);
        }

        // 4) No overlap with an existing non-cancelled booking.
        if (!bookingRepository.findOverlapping(start, end).isEmpty()) {
            return Optional.of(BookingFailure.OVERLAPS_EXISTING);
        }

        return Optional.empty();
    }

    // -------------------------------------------------------------- create booking --

    /**
     * Validate, persist, and publish event. Returns a {@link Result} carrying
     * either the saved {@link Booking} (success) or the {@link BookingFailure}
     * code (rejection). Exceptions are reserved for persistence / programming
     * errors, not for business rules.
     */
    @Transactional
    public Result<Booking, BookingFailure> tryBook(ReceptionistBookingDto dto) {
        LocalDateTime slotStart = dto.getSlotDate().atTime(dto.getSlotHour(), 0);
        int hours = dto.getDurationHours();

        Optional<BookingFailure> failure = checkAvailability(slotStart, hours);
        if (failure.isPresent()) {
            return Result.failure(failure.get());
        }

        BookingRequest b = new BookingRequest();
        b.setName(dto.getClientName());
        b.setEmail(dto.getClientEmail());
        b.setPhone(dto.getClientPhone());
        b.setTherapyType(dto.getTherapyType() == null ? null : dto.getTherapyType().display());
        b.setNotes(dto.getNotes());
        b.setSlotStart(slotStart);
        b.setSlotEnd(slotStart.plusHours(hours));
        b.setDurationHours(hours);
        b.setBookingSource(BookingSource.RECEPTIONIST);
        b.setStatus(SubmissionStatus.CONFIRMED);
        // Also fill the legacy free-text preferredDate field so email templates
        // and the admin list show something human-friendly.
        b.setPreferredDate(slotStart.format(
                java.time.format.DateTimeFormatter.ofPattern("EEE, d MMM yyyy 'at' h:mm a")));

        BookingRequest saved = bookingRepository.save(b);
        // Publish the domain event (carries Booking, not BookingRequest) so listeners stay persistence-ignorant.
        Booking domain = BookingMapper.toDomain(saved);
        eventPublisher.publishEvent(new BookingCreatedEvent(domain));
        return Result.success(domain);
    }

    /**
     * @deprecated transitional wrapper — call {@link #tryBook(ReceptionistBookingDto)}
     *             and pattern-match on the result instead. Throws on failure to keep
     *             the legacy controller path working until R7 lands.
     */
    @Deprecated(forRemoval = true)
    @Transactional
    public BookingRequest createReceptionistBooking(ReceptionistBookingDto dto) {
        Result<Booking, BookingFailure> result = tryBook(dto);
        if (result.isFailure()) {
            throw new IllegalArgumentException(result.error().userMessage());
        }
        // Convert the domain Booking back to the entity for legacy callers.
        return BookingMapper.toPersistence(result.value());
    }

    /** Cancel an existing booking — frees the slot back up. */
    @Transactional
    public Optional<BookingRequest> cancel(Long bookingId) {
        return bookingRepository.findById(bookingId).map(b -> {
            b.setStatus(SubmissionStatus.CANCELLED);
            return bookingRepository.save(b);
        });
    }

}
