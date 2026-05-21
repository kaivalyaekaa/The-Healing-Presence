package com.healingpresence.service;

import com.healingpresence.domain.BookingRequest;
import com.healingpresence.domain.BookingSource;
import com.healingpresence.domain.SubmissionStatus;
import com.healingpresence.dto.ReceptionistBookingDto;
import com.healingpresence.event.BookingSubmittedEvent;
import com.healingpresence.repository.BookingRequestRepository;
import com.healingpresence.scheduler.OfficeHours;
import com.healingpresence.scheduler.TimeSlot;
import com.healingpresence.scheduler.TimeSlot.SlotStatus;
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
        return validate(start, hours).isEmpty();
    }

    /**
     * Returns a list of human-readable validation problems with the proposed booking.
     * Empty list means the booking is valid.
     */
    @Transactional(readOnly = true)
    public List<String> validate(LocalDateTime start, int hours) {
        List<String> problems = new ArrayList<>();
        if (hours != 1 && hours != 2) {
            problems.add("Duration must be 1 or 2 hours.");
            return problems;
        }
        int startHour = start.getHour();

        // 1) Slot start must be a valid office hour.
        if (!OfficeHours.validStartHours().contains(startHour)) {
            problems.add("Slot start time (" + start.toLocalTime() + ") is outside office hours or during lunch.");
            return problems;
        }

        // 2) The window [start, start+hours) must not overlap lunch.
        //    Standard interval-overlap: A.start < B.end && A.end > B.start.
        LocalDateTime end = start.plusHours(hours);
        LocalTime lunchStart = LocalTime.of(OfficeHours.LUNCH_START, 0);
        LocalTime lunchEnd = LocalTime.of(OfficeHours.LUNCH_END, 0);
        if (start.toLocalTime().isBefore(lunchEnd) && end.toLocalTime().isAfter(lunchStart)) {
            problems.add("Cannot book " + hours + " hours at " + formatHour(startHour)
                    + " — would cross the 1–2 PM lunch break.");
        }

        // 3) The end must be at or before close.
        if (end.toLocalTime().isAfter(LocalTime.of(OfficeHours.CLOSE_HOUR, 0))
                || (end.toLocalDate().isAfter(start.toLocalDate()))) {
            problems.add("Cannot book " + hours + " hours at " + formatHour(startHour)
                    + " — would extend past office close at " + OfficeHours.CLOSE_HOUR + ":00.");
        }

        // 4) No overlap with an existing non-cancelled booking.
        List<BookingRequest> overlapping = bookingRepository.findOverlapping(start, end);
        if (!overlapping.isEmpty()) {
            problems.add("Slot conflicts with an existing booking at "
                    + overlapping.get(0).getSlotStart().toLocalTime() + ".");
        }

        return problems;
    }

    // -------------------------------------------------------------- create booking --

    /**
     * Validate, persist, and publish event. Status defaults to {@link SubmissionStatus#CONFIRMED}.
     * Throws {@link IllegalArgumentException} listing problems if validation fails.
     */
    @Transactional
    public BookingRequest createReceptionistBooking(ReceptionistBookingDto dto) {
        LocalDateTime slotStart = dto.getSlotDate().atTime(dto.getSlotHour(), 0);
        int hours = dto.getDurationHours();

        List<String> problems = validate(slotStart, hours);
        if (!problems.isEmpty()) {
            throw new IllegalArgumentException(String.join(" ", problems));
        }

        BookingRequest b = new BookingRequest();
        b.setName(dto.getClientName());
        b.setEmail(dto.getClientEmail());
        b.setPhone(dto.getClientPhone());
        b.setTherapyType(dto.getTherapyType());
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
        eventPublisher.publishEvent(new BookingSubmittedEvent(saved));
        return saved;
    }

    /** Cancel an existing booking — frees the slot back up. */
    @Transactional
    public Optional<BookingRequest> cancel(Long bookingId) {
        return bookingRepository.findById(bookingId).map(b -> {
            b.setStatus(SubmissionStatus.CANCELLED);
            return bookingRepository.save(b);
        });
    }

    // -------------------------------------------------------------- helpers --

    private static String formatHour(int hour) {
        int display = (hour == 0) ? 12 : (hour > 12 ? hour - 12 : hour);
        String suffix = hour < 12 ? "AM" : "PM";
        return display + " " + suffix;
    }
}
