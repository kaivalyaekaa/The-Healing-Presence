package in.thehealingpresence.notification;

import in.thehealingpresence.booking.domain.Booking;
import in.thehealingpresence.booking.event.BookingCreatedEvent;
import in.thehealingpresence.calendar.CalendarPort;
import in.thehealingpresence.domain.BookingRequest;
import in.thehealingpresence.repository.BookingRequestRepository;
import in.thehealingpresence.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

/**
 * Side-effects for receptionist bookings: email confirmation + Google Calendar
 * push. Listens to {@link BookingCreatedEvent} only (which carries the domain
 * {@link Booking} record, by definition a {@code RECEPTIONIST} booking).
 *
 * <p>Decoupled from any specific calendar provider via the {@link CalendarPort}
 * interface. Decoupled from the persistence layer via the {@link Booking}
 * domain record — but the followup save of {@code googleEventId} still uses
 * {@link BookingRequestRepository} directly because that's the persistence
 * boundary.
 *
 * <p>Side-effect failures are logged but never rolled back — the booking is
 * the source of truth, mail and calendar are best-effort.
 */
@Component
public class BookingNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(BookingNotificationListener.class);

    private final EmailService emailService;
    private final CalendarPort calendar;
    private final BookingRequestRepository bookingRepository;

    public BookingNotificationListener(EmailService emailService,
                                       CalendarPort calendar,
                                       BookingRequestRepository bookingRepository) {
        this.emailService = emailService;
        this.calendar = calendar;
        this.bookingRepository = bookingRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onBookingCreated(BookingCreatedEvent event) {
        Booking b = event.booking();
        log.debug("Booking listener: receptionist booking #{} for {}", b.id(), b.clientName());

        // 1) Email — admin notification + client thank-you.
        emailService.notifyAdmin(b);
        emailService.sendThankYou(b.clientEmail(), b.clientName(), "session booking");

        // 2) Google Calendar push — best-effort.
        try {
            Optional<String> eventId = calendar.pushBooking(b);
            eventId.ifPresent(id -> persistEventId(b.id(), id));
        } catch (Exception e) {
            log.error("Google Calendar push failed for booking {} ({}): {}",
                    b.id(), b.clientName(), e.getMessage(), e);
        }
    }

    /**
     * After a successful push, persist the returned event id onto the same
     * BookingRequest row so the cancellation flow can find it later.
     */
    private void persistEventId(Long bookingId, String eventId) {
        try {
            BookingRequest r = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new IllegalStateException(
                            "BookingRequest " + bookingId + " disappeared between commit and listener"));
            r.setGoogleEventId(eventId);
            bookingRepository.save(r);
        } catch (Exception saveEx) {
            // The booking itself is already committed; only the googleEventId failed to land.
            // Visible in the admin booking-detail view as missing event id. ERROR so it's loud.
            log.error("Could not persist googleEventId for booking {} (calendar event {} created OK, but db update failed): {}",
                    bookingId, eventId, saveEx.getMessage(), saveEx);
        }
    }
}
