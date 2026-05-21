package in.thehealingpresence.enquiry;

import in.thehealingpresence.domain.BookingRequest;
import in.thehealingpresence.domain.BookingSource;
import in.thehealingpresence.domain.SubmissionStatus;
import in.thehealingpresence.enquiry.domain.Enquiry;
import in.thehealingpresence.enquiry.domain.EnquiryStatus;
import in.thehealingpresence.enquiry.domain.TherapyType;

/**
 * Bidirectional translation between the persistence-layer {@link BookingRequest}
 * entity and the {@link Enquiry} domain record. Only the public-form fields are
 * mapped here — slot fields ({@code slotStart}, {@code slotEnd},
 * {@code durationHours}, {@code googleEventId}) are explicitly ignored on read
 * and forced null on write.
 *
 * <p>This class is the only place in the codebase that is allowed to touch the
 * {@code BookingRequest} entity directly. All services depend on
 * {@link Enquiry} via the {@link EnquiryRepository} facade.
 */
public final class EnquiryMapper {

    private EnquiryMapper() {
        // utility class
    }

    /**
     * @throws IllegalArgumentException if the row's {@code bookingSource} is RECEPTIONIST —
     *                                  the caller asked for the wrong bounded context.
     */
    public static Enquiry toDomain(BookingRequest e) {
        if (e == null) return null;
        if (e.getBookingSource() == BookingSource.RECEPTIONIST) {
            throw new IllegalArgumentException(
                    "BookingRequest id=" + e.getId() + " is a RECEPTIONIST booking, not an enquiry. "
                            + "Use BookingMapper.toDomain instead.");
        }
        return new Enquiry(
                e.getId(),
                e.getName(),
                e.getEmail(),
                e.getPhone(),
                e.getPreferredDate(),
                TherapyType.fromString(e.getTherapyType()).orElse(TherapyType.OTHER),
                e.getNotes(),
                toEnquiryStatus(e.getStatus()),
                e.getCreatedAt()
        );
    }

    /**
     * Persists an {@link Enquiry} into a fresh or existing {@link BookingRequest}.
     * Slot fields are explicitly nulled.
     */
    public static BookingRequest toPersistence(Enquiry domain) {
        if (domain == null) return null;
        BookingRequest e = new BookingRequest();
        e.setId(domain.id());
        e.setName(domain.name());
        e.setEmail(domain.email());
        e.setPhone(domain.phone());
        e.setPreferredDate(domain.preferredDate());
        e.setTherapyType(domain.therapyType() == null ? null : domain.therapyType().display());
        e.setNotes(domain.notes());
        e.setStatus(toSubmissionStatus(domain.status()));
        e.setBookingSource(BookingSource.PUBLIC_FORM);
        // slot fields stay null — this is an enquiry, not a booking
        e.setSlotStart(null);
        e.setSlotEnd(null);
        e.setDurationHours(null);
        e.setGoogleEventId(null);
        return e;
    }

    private static EnquiryStatus toEnquiryStatus(SubmissionStatus s) {
        if (s == null) return EnquiryStatus.NEW;
        return switch (s) {
            case NEW -> EnquiryStatus.NEW;
            case READ -> EnquiryStatus.READ;
            case REPLIED -> EnquiryStatus.REPLIED;
            // CONFIRMED/CANCELLED are booking-only states — defensive fallback:
            case CONFIRMED, CANCELLED -> EnquiryStatus.READ;
        };
    }

    private static SubmissionStatus toSubmissionStatus(EnquiryStatus s) {
        if (s == null) return SubmissionStatus.NEW;
        return switch (s) {
            case NEW -> SubmissionStatus.NEW;
            case READ -> SubmissionStatus.READ;
            case REPLIED -> SubmissionStatus.REPLIED;
        };
    }
}
