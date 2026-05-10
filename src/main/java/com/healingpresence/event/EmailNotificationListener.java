package com.healingpresence.event;

import com.healingpresence.domain.BookingRequest;
import com.healingpresence.domain.ContactSubmission;
import com.healingpresence.domain.SpaceEnquiry;
import com.healingpresence.service.EmailService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Fires emails after the persistence transaction commits, so a mail-server
 * outage never rolls back a successful save and a rollback never sends a mail.
 */
@Component
public class EmailNotificationListener {

    private final EmailService emailService;

    public EmailNotificationListener(EmailService emailService) {
        this.emailService = emailService;
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
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onEnquirySubmitted(EnquirySubmittedEvent event) {
        SpaceEnquiry e = event.enquiry();
        emailService.notifyAdmin(e);
        emailService.sendThankYou(e.getEmail(), e.getName(), "space enquiry");
    }
}
