package in.thehealingpresence.notification;

import in.thehealingpresence.domain.BookingRequest;
import in.thehealingpresence.domain.BookingSource;
import in.thehealingpresence.domain.ContactSubmission;
import in.thehealingpresence.domain.SpaceEnquiry;
import in.thehealingpresence.event.BookingSubmittedEvent;
import in.thehealingpresence.event.ContactSubmittedEvent;
import in.thehealingpresence.event.EnquirySubmittedEvent;
import in.thehealingpresence.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Email side-effects for public-form submissions (contact, booking-request, space).
 *
 * <p>Listens to three events:
 * <ul>
 *   <li>{@link ContactSubmittedEvent} — every contact form post.</li>
 *   <li>{@link EnquirySubmittedEvent} — every space-rental enquiry.</li>
 *   <li>{@link BookingSubmittedEvent} where {@code bookingSource == PUBLIC_FORM} —
 *       a public-form booking request with free-text {@code preferredDate}.
 *       Receptionist bookings are handled by
 *       {@link BookingNotificationListener} instead.</li>
 * </ul>
 *
 * <p>No calendar push — public-form enquiries use free-text dates that would
 * pollute Upma's calendar. The receptionist confirms them manually and (if
 * they accept the slot) re-enters them via {@code /reception/new}.
 */
@Component
public class EnquiryNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(EnquiryNotificationListener.class);

    private final EmailService emailService;

    public EnquiryNotificationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onContactSubmitted(ContactSubmittedEvent event) {
        ContactSubmission s = event.submission();
        log.debug("Enquiry listener: contact from {}", s.getEmail());
        emailService.notifyAdmin(s);
        emailService.sendThankYou(s.getEmail(), s.getFirstName(), "message");
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onBookingSubmitted(BookingSubmittedEvent event) {
        BookingRequest r = event.request();
        if (r.getBookingSource() != BookingSource.PUBLIC_FORM) {
            // Receptionist bookings now publish BookingCreatedEvent — this listener
            // shouldn't ever see them, but guard defensively.
            return;
        }
        log.debug("Enquiry listener: public-form booking from {}", r.getEmail());
        emailService.notifyAdmin(r);
        emailService.sendThankYou(r.getEmail(), r.getName(), "session booking");
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSpaceEnquirySubmitted(EnquirySubmittedEvent event) {
        SpaceEnquiry e = event.enquiry();
        log.debug("Enquiry listener: space enquiry from {}", e.getEmail());
        emailService.notifyAdmin(e);
        emailService.sendThankYou(e.getEmail(), e.getName(), "space enquiry");
    }
}
