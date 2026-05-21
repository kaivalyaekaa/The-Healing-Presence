package in.thehealingpresence.domain;

/**
 * Lifecycle status for {@link ContactSubmission}, {@link BookingRequest}, {@link SpaceEnquiry}.
 * <p>
 * Public-form submissions land as {@link #NEW}.
 * Receptionist-created bookings land as {@link #CONFIRMED} (slot reserved).
 * {@link #CANCELLED} frees the slot back up in the day-grid scheduler.
 */
public enum SubmissionStatus {
    NEW,
    READ,
    REPLIED,
    CONFIRMED,
    CANCELLED
}
