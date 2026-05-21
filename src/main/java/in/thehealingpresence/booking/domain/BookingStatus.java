package in.thehealingpresence.booking.domain;

/**
 * Lifecycle states for receptionist-created slot bookings.
 * Maps onto the existing persistence-layer {@code SubmissionStatus} via
 * {@link in.thehealingpresence.booking.BookingMapper}; only {@code CONFIRMED}
 * and {@code CANCELLED} apply to bookings.
 */
public enum BookingStatus {
    CONFIRMED,
    CANCELLED
}
