package in.thehealingpresence.enquiry.domain;

import java.time.Instant;

/**
 * A public-form enquiry — the bounded context of public booking requests sent
 * through {@code /therapy}. Has no slot fields; {@code preferredDate} is the
 * free-text the client typed (e.g. "next Tuesday afternoon" or "2026-06-12").
 *
 * <p>Persistence-ignorant: services consume {@code Enquiry}, never the
 * underlying {@code BookingRequest} JPA entity. The
 * {@link in.thehealingpresence.enquiry.EnquiryMapper} is the only place that
 * knows about the persistence representation.
 */
public record Enquiry(
        Long id,
        String name,
        String email,
        String phone,
        String preferredDate,
        TherapyType therapyType,
        String notes,
        EnquiryStatus status,
        Instant createdAt
) {
    /** Convenience constructor for newly-built enquiries (no id, no timestamp, status = NEW). */
    public static Enquiry draft(String name, String email, String phone, String preferredDate,
                                TherapyType therapyType, String notes) {
        return new Enquiry(null, name, email, phone, preferredDate, therapyType, notes,
                EnquiryStatus.NEW, null);
    }
}
