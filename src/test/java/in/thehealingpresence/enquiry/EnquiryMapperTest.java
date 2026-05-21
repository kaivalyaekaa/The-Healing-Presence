package in.thehealingpresence.enquiry;

import in.thehealingpresence.domain.BookingRequest;
import in.thehealingpresence.domain.BookingSource;
import in.thehealingpresence.domain.SubmissionStatus;
import in.thehealingpresence.enquiry.domain.Enquiry;
import in.thehealingpresence.enquiry.domain.EnquiryStatus;
import in.thehealingpresence.enquiry.domain.TherapyType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnquiryMapperTest {

    @Test
    void roundTripPublicFormBookingPreservesFieldsAndLeavesSlotFieldsNull() {
        BookingRequest original = new BookingRequest();
        original.setId(42L);
        original.setName("Aadi Kapoor");
        original.setEmail("aadi@example.com");
        original.setPhone("+91 9876543210");
        original.setPreferredDate("Next Tuesday afternoon");
        original.setTherapyType("Hypnotherapy");
        original.setNotes("First-time client, prefers evening");
        original.setStatus(SubmissionStatus.NEW);
        original.setBookingSource(BookingSource.PUBLIC_FORM);

        Enquiry domain = EnquiryMapper.toDomain(original);
        assertThat(domain.id()).isEqualTo(42L);
        assertThat(domain.name()).isEqualTo("Aadi Kapoor");
        assertThat(domain.therapyType()).isEqualTo(TherapyType.HYPNOTHERAPY);
        assertThat(domain.preferredDate()).isEqualTo("Next Tuesday afternoon");
        assertThat(domain.status()).isEqualTo(EnquiryStatus.NEW);

        BookingRequest roundTrip = EnquiryMapper.toPersistence(domain);
        assertThat(roundTrip.getName()).isEqualTo(original.getName());
        assertThat(roundTrip.getEmail()).isEqualTo(original.getEmail());
        assertThat(roundTrip.getPhone()).isEqualTo(original.getPhone());
        assertThat(roundTrip.getPreferredDate()).isEqualTo(original.getPreferredDate());
        assertThat(roundTrip.getTherapyType()).isEqualTo("Hypnotherapy");
        assertThat(roundTrip.getNotes()).isEqualTo(original.getNotes());
        assertThat(roundTrip.getStatus()).isEqualTo(SubmissionStatus.NEW);
        assertThat(roundTrip.getBookingSource()).isEqualTo(BookingSource.PUBLIC_FORM);

        // Slot fields must be null — this is an enquiry, not a booking.
        assertThat(roundTrip.getSlotStart()).isNull();
        assertThat(roundTrip.getSlotEnd()).isNull();
        assertThat(roundTrip.getDurationHours()).isNull();
        assertThat(roundTrip.getGoogleEventId()).isNull();
    }

    @Test
    void toDomainRejectsReceptionistRows() {
        BookingRequest r = new BookingRequest();
        r.setId(99L);
        r.setBookingSource(BookingSource.RECEPTIONIST);
        assertThatThrownBy(() -> EnquiryMapper.toDomain(r))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("RECEPTIONIST booking, not an enquiry");
    }

    @Test
    void unknownTherapyTypeStringFallsBackToOther() {
        BookingRequest original = new BookingRequest();
        original.setBookingSource(BookingSource.PUBLIC_FORM);
        original.setStatus(SubmissionStatus.NEW);
        original.setTherapyType("Some Wellness Modality That Doesn't Match The Enum");

        Enquiry domain = EnquiryMapper.toDomain(original);
        assertThat(domain.therapyType()).isEqualTo(TherapyType.OTHER);
    }
}
