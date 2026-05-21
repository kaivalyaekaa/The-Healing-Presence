package in.thehealingpresence.booking.domain;

import in.thehealingpresence.enquiry.domain.TherapyType;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * A receptionist-created slot booking — the bounded context that the slot
 * scheduler operates on. Every booking has concrete slot times (non-null),
 * a known therapy type, and a CONFIRMED status by default.
 *
 * <p>Persistence-ignorant: services consume {@code Booking}, never the
 * underlying {@code BookingRequest} JPA entity. The
 * {@link in.thehealingpresence.booking.BookingMapper} is the only place that
 * knows about the persistence representation.
 *
 * @param googleEventId Populated only after a successful Google Calendar push.
 *                      Null on a freshly-created booking; the calendar
 *                      notification listener back-fills it on AFTER_COMMIT.
 */
public record Booking(
        Long id,
        String clientName,
        String clientEmail,
        String clientPhone,
        LocalDateTime slotStart,
        LocalDateTime slotEnd,
        int durationHours,
        TherapyType therapyType,
        String notes,
        BookingStatus status,
        String googleEventId,
        Instant createdAt
) {
    public Booking {
        if (slotStart == null) throw new IllegalArgumentException("slotStart must not be null");
        if (slotEnd == null) throw new IllegalArgumentException("slotEnd must not be null");
        if (durationHours < 1 || durationHours > 2)
            throw new IllegalArgumentException("durationHours must be 1 or 2, got " + durationHours);
        if (therapyType == null) throw new IllegalArgumentException("therapyType must not be null");
        if (status == null) throw new IllegalArgumentException("status must not be null");
    }

    /** Convenience constructor for newly-scheduled bookings. */
    public static Booking newConfirmed(String clientName, String clientEmail, String clientPhone,
                                        LocalDateTime slotStart, LocalDateTime slotEnd,
                                        int durationHours, TherapyType therapyType, String notes) {
        return new Booking(null, clientName, clientEmail, clientPhone, slotStart, slotEnd,
                durationHours, therapyType, notes, BookingStatus.CONFIRMED, null, null);
    }

    /** Returns a copy of this booking with {@code googleEventId} set. */
    public Booking withGoogleEventId(String eventId) {
        return new Booking(id, clientName, clientEmail, clientPhone, slotStart, slotEnd,
                durationHours, therapyType, notes, status, eventId, createdAt);
    }
}
