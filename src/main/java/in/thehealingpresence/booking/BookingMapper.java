package in.thehealingpresence.booking;

import in.thehealingpresence.booking.domain.Booking;
import in.thehealingpresence.booking.domain.BookingStatus;
import in.thehealingpresence.domain.BookingRequest;
import in.thehealingpresence.domain.BookingSource;
import in.thehealingpresence.domain.SubmissionStatus;
import in.thehealingpresence.enquiry.domain.TherapyType;

/**
 * Bidirectional translation between the persistence-layer {@link BookingRequest}
 * entity and the {@link Booking} domain record. Bookings always have non-null
 * slot fields; the mapper enforces this on read.
 *
 * <p>This class is the only place in the codebase that is allowed to touch the
 * {@code BookingRequest} entity directly. All services depend on
 * {@link Booking} via the {@link BookingRepository} facade.
 */
public final class BookingMapper {

    private BookingMapper() {
        // utility class
    }

    /**
     * @throws IllegalArgumentException if the row's {@code bookingSource} is PUBLIC_FORM
     *                                  or any of the slot fields are null — the caller
     *                                  asked for the wrong bounded context.
     */
    public static Booking toDomain(BookingRequest r) {
        if (r == null) return null;
        if (r.getBookingSource() != BookingSource.RECEPTIONIST) {
            throw new IllegalArgumentException(
                    "BookingRequest id=" + r.getId() + " is a " + r.getBookingSource()
                            + " row, not a receptionist booking. Use EnquiryMapper.toDomain instead.");
        }
        if (r.getSlotStart() == null || r.getSlotEnd() == null || r.getDurationHours() == null) {
            throw new IllegalArgumentException(
                    "BookingRequest id=" + r.getId() + " is a RECEPTIONIST row but has null slot fields. "
                            + "Data corruption — likely written without going through SlotSchedulerService.");
        }
        TherapyType therapy = TherapyType.fromString(r.getTherapyType())
                .orElse(TherapyType.OTHER);
        return new Booking(
                r.getId(),
                r.getName(),
                r.getEmail(),
                r.getPhone(),
                r.getSlotStart(),
                r.getSlotEnd(),
                r.getDurationHours(),
                therapy,
                r.getNotes(),
                toBookingStatus(r.getStatus()),
                r.getGoogleEventId(),
                r.getCreatedAt()
        );
    }

    /** Persists a {@link Booking} into a fresh or existing {@link BookingRequest}. */
    public static BookingRequest toPersistence(Booking b) {
        if (b == null) return null;
        BookingRequest r = new BookingRequest();
        r.setId(b.id());
        r.setName(b.clientName());
        r.setEmail(b.clientEmail());
        r.setPhone(b.clientPhone());
        r.setSlotStart(b.slotStart());
        r.setSlotEnd(b.slotEnd());
        r.setDurationHours(b.durationHours());
        r.setTherapyType(b.therapyType().display());
        r.setNotes(b.notes());
        r.setStatus(toSubmissionStatus(b.status()));
        r.setBookingSource(BookingSource.RECEPTIONIST);
        r.setGoogleEventId(b.googleEventId());
        // preferredDate stays null — receptionist booking uses the concrete slot, not free text
        r.setPreferredDate(null);
        return r;
    }

    private static BookingStatus toBookingStatus(SubmissionStatus s) {
        if (s == null) return BookingStatus.CONFIRMED;
        return switch (s) {
            case CONFIRMED -> BookingStatus.CONFIRMED;
            case CANCELLED -> BookingStatus.CANCELLED;
            // NEW/READ/REPLIED are enquiry-only states — defensive fallback:
            case NEW, READ, REPLIED -> BookingStatus.CONFIRMED;
        };
    }

    private static SubmissionStatus toSubmissionStatus(BookingStatus s) {
        return switch (s) {
            case CONFIRMED -> SubmissionStatus.CONFIRMED;
            case CANCELLED -> SubmissionStatus.CANCELLED;
        };
    }
}
