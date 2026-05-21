package in.thehealingpresence.enquiry.domain;

/**
 * Lifecycle states for public-form enquiries (contact, booking-request, space).
 * Maps onto the existing persistence-layer {@code SubmissionStatus} via
 * {@link in.thehealingpresence.enquiry.EnquiryMapper}.
 */
public enum EnquiryStatus {
    NEW,
    READ,
    REPLIED
}
