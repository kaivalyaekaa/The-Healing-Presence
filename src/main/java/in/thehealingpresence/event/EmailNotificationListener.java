package in.thehealingpresence.event;

import in.thehealingpresence.domain.BookingRequest;
import in.thehealingpresence.domain.BookingSource;
import in.thehealingpresence.domain.ContactSubmission;
import in.thehealingpresence.domain.SpaceEnquiry;
import in.thehealingpresence.repository.BookingRequestRepository;
import in.thehealingpresence.service.EmailService;
import in.thehealingpresence.service.GoogleCalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Fires side-effects after the persistence transaction commits.
 * <ul>
 *   <li>Email notifications for all form submissions (contact / booking / enquiry).</li>
 *   <li>Google Calendar push for RECEPTIONIST-source bookings only (public-form bookings
 *       use a free-text preferredDate so we don't pollute Upma's calendar with rough dates).</li>
 * </ul>
 *
 * Side-effect failures are logged but never rolled back — the booking is the source of truth,
 * mail and calendar are best-effort.
 */
@Component
public class EmailNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationListener.class);

    private final EmailService emailService;
    private final GoogleCalendarService googleCalendarService;
    private final BookingRequestRepository bookingRepository;

    public EmailNotificationListener(EmailService emailService,
                                     GoogleCalendarService googleCalendarService,
                                     BookingRequestRepository bookingRepository) {
        this.emailService = emailService;
        this.googleCalendarService = googleCalendarService;
        this.bookingRepository = bookingRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onContactSubmitted(ContactSubmittedEvent event) {
        ContactSubmission s = event.submission();
        emailService.notifyAdmin(s);
        emailService.sendThankYou(s.getEmail(), s.getFirstName(), "message");
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onBookingSubmitted(BookingSubmittedEvent event) {
        BookingRequest r = event.request();
        emailService.notifyAdmin(r);
        emailService.sendThankYou(r.getEmail(), r.getName(), "session booking");

        // Push to Google Calendar only for receptionist-created bookings with a real slot.
        if (r.getBookingSource() == BookingSource.RECEPTIONIST
                && r.getSlotStart() != null && r.getSlotEnd() != null) {
            try {
                String eventId = googleCalendarService.pushEvent(r);
                if (eventId != null && !eventId.isBlank()) {
                    r.setGoogleEventId(eventId);
                    try {
                        bookingRepository.save(r);
                    } catch (Exception saveEx) {
                        // The original booking is already committed; this save is the followup
                        // to persist the Google event id. Failure leaves googleEventId=null —
                        // visible in the admin booking-detail view. ERROR so it shows up in logs.
                        log.error("Could not persist googleEventId for booking {} (calendar event {} created OK, but db update failed): {}",
                                r.getId(), eventId, saveEx.getMessage(), saveEx);
                    }
                }
            } catch (Exception e) {
                log.error("Google Calendar push failed for booking {} ({}): {}",
                        r.getId(), r.getName(), e.getMessage(), e);
            }
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onEnquirySubmitted(EnquirySubmittedEvent event) {
        SpaceEnquiry e = event.enquiry();
        emailService.notifyAdmin(e);
        emailService.sendThankYou(e.getEmail(), e.getName(), "space enquiry");
    }
}
