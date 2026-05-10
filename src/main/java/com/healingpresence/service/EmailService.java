package com.healingpresence.service;

import com.healingpresence.domain.BookingRequest;
import com.healingpresence.domain.ContactSubmission;
import com.healingpresence.domain.SpaceEnquiry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${app.notifications.to}")
    private String adminEmail;

    @Value("${app.notifications.from}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void notifyAdmin(ContactSubmission s) {
        send(adminEmail,
                "New Contact Submission from " + s.getFirstName(),
                "New contact form submission received:\n\n" +
                        "Name: " + s.getFirstName() + " " + nullSafe(s.getLastName()) + "\n" +
                        "Email: " + s.getEmail() + "\n" +
                        "Phone: " + nullSafe(s.getPhone()) + "\n" +
                        "Message:\n" + s.getMessage());
    }

    @Async
    public void notifyAdmin(BookingRequest r) {
        send(adminEmail,
                "New Session Booking from " + r.getName(),
                "New session booking received:\n\n" +
                        "Name: " + r.getName() + "\n" +
                        "Email: " + r.getEmail() + "\n" +
                        "Phone: " + nullSafe(r.getPhone()) + "\n" +
                        "Therapy type: " + nullSafe(r.getTherapyType()) + "\n" +
                        "Preferred date: " + nullSafe(r.getPreferredDate()) + "\n" +
                        "Notes:\n" + nullSafe(r.getNotes()));
    }

    @Async
    public void notifyAdmin(SpaceEnquiry e) {
        send(adminEmail,
                "New Space Enquiry from " + e.getName(),
                "New space rental enquiry received:\n\n" +
                        "Name: " + e.getName() + "\n" +
                        "Email: " + e.getEmail() + "\n" +
                        "Phone: " + nullSafe(e.getPhone()) + "\n" +
                        "Event type: " + nullSafe(e.getEventType()) + "\n" +
                        "Preferred date: " + nullSafe(e.getPreferredDate()) + "\n" +
                        "Attendees: " + (e.getAttendees() == null ? "N/A" : e.getAttendees()) + "\n" +
                        "Message:\n" + nullSafe(e.getMessage()));
    }

    @Async
    public void sendThankYou(String recipientEmail, String firstName, String formContext) {
        String greeting = (firstName == null || firstName.isBlank()) ? "Dear Friend" : "Dear " + firstName;
        send(recipientEmail,
                "Thank you for reaching out — The Healing Presence",
                greeting + ",\n\n" +
                        "Thank you for your " + formContext + " with The Healing Presence. " +
                        "We have received your message and will get back to you shortly.\n\n" +
                        "Warm regards,\n" +
                        "The Healing Presence Team");
    }

    private void send(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Mail sent to {}: {}", to, subject);
        } catch (Exception ex) {
            log.warn("Failed to send mail to {} ({}): {}", to, subject, ex.getMessage());
        }
    }

    private static String nullSafe(String s) {
        return s == null ? "N/A" : s;
    }
}
