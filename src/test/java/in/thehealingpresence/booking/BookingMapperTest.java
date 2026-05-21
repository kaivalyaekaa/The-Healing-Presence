package in.thehealingpresence.booking;

import in.thehealingpresence.booking.domain.Booking;
import in.thehealingpresence.booking.domain.BookingStatus;
import in.thehealingpresence.domain.BookingRequest;
import in.thehealingpresence.domain.BookingSource;
import in.thehealingpresence.domain.SubmissionStatus;
import in.thehealingpresence.enquiry.domain.TherapyType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookingMapperTest {

    @Test
    void roundTripReceptionistBookingPreservesEverything() {
        LocalDateTime slotStart = LocalDateTime.of(2026, 6, 1, 10, 0);
        BookingRequest original = new BookingRequest();
        original.setId(7L);
        original.setName("Aadi Kapoor");
        original.setEmail("aadi@example.com");
        original.setPhone("+91 9876543210");
        original.setSlotStart(slotStart);
        original.setSlotEnd(slotStart.plusHours(2));
        original.setDurationHours(2);
        original.setTherapyType("Crystal Healing");
        original.setNotes("Bring quartz");
        original.setStatus(SubmissionStatus.CONFIRMED);
        original.setBookingSource(BookingSource.RECEPTIONIST);
        original.setGoogleEventId("evt-abc-123");

        Booking domain = BookingMapper.toDomain(original);
        assertThat(domain.id()).isEqualTo(7L);
        assertThat(domain.clientName()).isEqualTo("Aadi Kapoor");
        assertThat(domain.slotStart()).isEqualTo(slotStart);
        assertThat(domain.slotEnd()).isEqualTo(slotStart.plusHours(2));
        assertThat(domain.durationHours()).isEqualTo(2);
        assertThat(domain.therapyType()).isEqualTo(TherapyType.CRYSTAL_HEALING);
        assertThat(domain.status()).isEqualTo(BookingStatus.CONFIRMED);
        assertThat(domain.googleEventId()).isEqualTo("evt-abc-123");

        BookingRequest roundTrip = BookingMapper.toPersistence(domain);
        assertThat(roundTrip.getName()).isEqualTo(original.getName());
        assertThat(roundTrip.getSlotStart()).isEqualTo(original.getSlotStart());
        assertThat(roundTrip.getSlotEnd()).isEqualTo(original.getSlotEnd());
        assertThat(roundTrip.getDurationHours()).isEqualTo(original.getDurationHours());
        assertThat(roundTrip.getTherapyType()).isEqualTo("Crystal Healing");
        assertThat(roundTrip.getStatus()).isEqualTo(SubmissionStatus.CONFIRMED);
        assertThat(roundTrip.getBookingSource()).isEqualTo(BookingSource.RECEPTIONIST);
        assertThat(roundTrip.getGoogleEventId()).isEqualTo("evt-abc-123");
        // preferredDate must be null for receptionist bookings (the slot fields are the source of truth).
        assertThat(roundTrip.getPreferredDate()).isNull();
    }

    @Test
    void toDomainRejectsPublicFormRows() {
        BookingRequest r = new BookingRequest();
        r.setId(99L);
        r.setBookingSource(BookingSource.PUBLIC_FORM);
        assertThatThrownBy(() -> BookingMapper.toDomain(r))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("PUBLIC_FORM row, not a receptionist booking");
    }

    @Test
    void toDomainRejectsReceptionistRowsWithNullSlotFields() {
        BookingRequest r = new BookingRequest();
        r.setId(99L);
        r.setBookingSource(BookingSource.RECEPTIONIST);
        // Slot fields not set — data corruption signal.
        assertThatThrownBy(() -> BookingMapper.toDomain(r))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("null slot fields")
                .hasMessageContaining("Data corruption");
    }
}
