package com.healingpresence.service;

import com.healingpresence.TestSecurityConfig;
import com.healingpresence.domain.BookingRequest;
import com.healingpresence.domain.BookingSource;
import com.healingpresence.domain.SubmissionStatus;
import com.healingpresence.dto.ReceptionistBookingDto;
import com.healingpresence.repository.BookingRequestRepository;
import com.healingpresence.scheduler.TimeSlot;
import com.healingpresence.scheduler.TimeSlot.SlotStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@Transactional
class SlotSchedulerServiceTest {

    @Autowired
    private SlotSchedulerService scheduler;

    @Autowired
    private BookingRequestRepository repository;

    private final LocalDate FUTURE = LocalDate.now().plusDays(7); // safely in the future

    @BeforeEach
    void clean() {
        repository.deleteAll();
    }

    @Test
    void emptyDayHas7AvailableSlotsAndOneLunch() {
        List<TimeSlot> slots = scheduler.getDaySlots(FUTURE);
        // 9 hour-slots from 10 to 18 inclusive (10, 11, 12, 13, 14, 15, 16, 17, 18)
        assertThat(slots).hasSize(9);
        assertThat(slots.stream().filter(s -> s.status() == SlotStatus.LUNCH).count()).isEqualTo(1);
        assertThat(slots.stream().filter(s -> s.status() == SlotStatus.AVAILABLE).count()).isEqualTo(8);
    }

    @Test
    void twoHourBookingAt10AmCascadeBlocks11Am() {
        scheduler.createReceptionistBooking(dto(FUTURE, 10, 2));
        List<TimeSlot> slots = scheduler.getDaySlots(FUTURE);
        TimeSlot tenAm = slotAtHour(slots, 10);
        TimeSlot elevenAm = slotAtHour(slots, 11);
        TimeSlot twelvePm = slotAtHour(slots, 12);
        assertThat(tenAm.status()).isEqualTo(SlotStatus.BOOKED);
        assertThat(tenAm.durationHours()).isEqualTo(2);
        assertThat(elevenAm.status()).isEqualTo(SlotStatus.BLOCKED_BY_CASCADE);
        assertThat(elevenAm.isClickable()).isFalse();
        assertThat(twelvePm.status()).isEqualTo(SlotStatus.AVAILABLE);
    }

    @Test
    void oneHourBookingAt10AmLeaves11AmAvailable() {
        scheduler.createReceptionistBooking(dto(FUTURE, 10, 1));
        List<TimeSlot> slots = scheduler.getDaySlots(FUTURE);
        assertThat(slotAtHour(slots, 10).status()).isEqualTo(SlotStatus.BOOKED);
        assertThat(slotAtHour(slots, 11).status()).isEqualTo(SlotStatus.AVAILABLE);
    }

    @Test
    void twoHourBookingAt12PmIsRejectedDueToLunchCrossover() {
        assertThatThrownBy(() -> scheduler.createReceptionistBooking(dto(FUTURE, 12, 2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("lunch");
    }

    @Test
    void oneHourBookingAt12PmIsAccepted() {
        BookingRequest saved = scheduler.createReceptionistBooking(dto(FUTURE, 12, 1));
        assertThat(saved.getStatus()).isEqualTo(SubmissionStatus.CONFIRMED);
        assertThat(saved.getBookingSource()).isEqualTo(BookingSource.RECEPTIONIST);
        assertThat(saved.getSlotEnd()).isEqualTo(FUTURE.atTime(13, 0));
    }

    @Test
    void twoHourBookingAt6PmIsRejectedDueToCloseHour() {
        assertThatThrownBy(() -> scheduler.createReceptionistBooking(dto(FUTURE, 18, 2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("close");
    }

    @Test
    void lunchHourIsAlwaysLunchStatus() {
        List<TimeSlot> slots = scheduler.getDaySlots(FUTURE);
        TimeSlot lunch = slotAtHour(slots, 13);
        assertThat(lunch.status()).isEqualTo(SlotStatus.LUNCH);
        assertThat(lunch.isClickable()).isFalse();
    }

    @Test
    void overlappingBookingIsRejected() {
        scheduler.createReceptionistBooking(dto(FUTURE, 15, 2)); // 3-5 PM
        assertThatThrownBy(() -> scheduler.createReceptionistBooking(dto(FUTURE, 16, 1))) // 4-5 PM overlap
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("conflicts");
    }

    @Test
    void cancellingABookingFreesTheSlot() {
        BookingRequest saved = scheduler.createReceptionistBooking(dto(FUTURE, 11, 2));
        assertThat(slotAtHour(scheduler.getDaySlots(FUTURE), 11).status()).isEqualTo(SlotStatus.BOOKED);

        scheduler.cancel(saved.getId());

        List<TimeSlot> after = scheduler.getDaySlots(FUTURE);
        assertThat(slotAtHour(after, 11).status()).isEqualTo(SlotStatus.AVAILABLE);
        assertThat(slotAtHour(after, 12).status()).isEqualTo(SlotStatus.AVAILABLE); // cascade lifted too
    }

    @Test
    void canBookGuardMatchesCreateBehavior() {
        LocalDateTime ok = FUTURE.atTime(10, 0);
        LocalDateTime lunchCross = FUTURE.atTime(12, 0); // with 2h
        LocalDateTime pastClose = FUTURE.atTime(18, 0);  // with 2h
        assertThat(scheduler.canBook(ok, 2)).isTrue();
        assertThat(scheduler.canBook(lunchCross, 2)).isFalse();
        assertThat(scheduler.canBook(pastClose, 2)).isFalse();
        assertThat(scheduler.canBook(lunchCross, 1)).isTrue(); // 12 PM 1h is fine
        assertThat(scheduler.canBook(pastClose, 1)).isTrue();  // 6 PM 1h is fine
    }

    // ------------------------------------------------------------ helpers --

    private static ReceptionistBookingDto dto(LocalDate date, int hour, int duration) {
        ReceptionistBookingDto d = new ReceptionistBookingDto();
        d.setClientName("Test Client");
        d.setClientEmail("test@example.com");
        d.setClientPhone("+91 9876543210");
        d.setSlotDate(date);
        d.setSlotHour(hour);
        d.setDurationHours(duration);
        d.setTherapyType("Hypnotherapy");
        d.setNotes("Test booking");
        return d;
    }

    private static TimeSlot slotAtHour(List<TimeSlot> slots, int hour) {
        return slots.stream()
                .filter(s -> s.start().getHour() == hour)
                .findFirst()
                .orElseThrow(() -> new AssertionError("No slot at hour " + hour));
    }
}
