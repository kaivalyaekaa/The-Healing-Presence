package in.thehealingpresence.service;

import in.thehealingpresence.TestSecurityConfig;
import in.thehealingpresence.domain.BookingRequest;
import in.thehealingpresence.domain.BookingSource;
import in.thehealingpresence.domain.SubmissionStatus;
import in.thehealingpresence.dto.ReceptionistBookingDto;
import in.thehealingpresence.repository.BookingRequestRepository;
import in.thehealingpresence.scheduling.SlotSchedulerService;
import in.thehealingpresence.scheduling.SlotStatus;
import in.thehealingpresence.scheduling.TimeSlot;
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
    void emptyDayHas6AvailableSlotsAndOneLunch() {
        List<TimeSlot> slots = scheduler.getDaySlots(FUTURE);
        // 7 hour-slots rendered: 10, 11, 12, LUNCH at 13, 14, 15, 16.
        // 17 (5 PM) is office-open but not a valid booking start; it's only
        // rendered when cascade-blocked by a 16:00 two-hour booking.
        assertThat(slots).hasSize(7);
        assertThat(slots.stream().filter(s -> s.status() == SlotStatus.LUNCH).count()).isEqualTo(1);
        assertThat(slots.stream().filter(s -> s.status() == SlotStatus.AVAILABLE).count()).isEqualTo(6);
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
    void bookingAt5PmIsRejectedBecauseNotAValidStart() {
        // 17:00 (5 PM) exists in office hours but is reachable only as the
        // cascade hour of a 16:00 two-hour booking. Direct booking is rejected.
        assertThatThrownBy(() -> scheduler.createReceptionistBooking(dto(FUTURE, 17, 1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("outside office hours");
    }

    @Test
    void bookingAt6PmIsRejectedBecauseOfficeIsClosed() {
        // 18:00 (6 PM) is the close hour itself — never a valid booking start.
        assertThatThrownBy(() -> scheduler.createReceptionistBooking(dto(FUTURE, 18, 1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("outside office hours");
    }

    @Test
    void twoHourBookingAt4PmEndsExactlyAtClose() {
        // 16:00 + 2h = 18:00 (close). The 5 PM cascade hour shows up in the grid.
        scheduler.createReceptionistBooking(dto(FUTURE, 16, 2));
        List<TimeSlot> slots = scheduler.getDaySlots(FUTURE);
        assertThat(slotAtHour(slots, 16).status()).isEqualTo(SlotStatus.BOOKED);
        assertThat(slotAtHour(slots, 16).durationHours()).isEqualTo(2);
        assertThat(slotAtHour(slots, 17).status()).isEqualTo(SlotStatus.BLOCKED_BY_CASCADE);
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
        LocalDateTime lunchCross = FUTURE.atTime(12, 0); // with 2h would cross lunch
        LocalDateTime fivePm = FUTURE.atTime(17, 0);     // not a valid start (cascade-only)
        LocalDateTime sixPm = FUTURE.atTime(18, 0);      // close hour itself
        assertThat(scheduler.canBook(ok, 2)).isTrue();
        assertThat(scheduler.canBook(lunchCross, 2)).isFalse();
        assertThat(scheduler.canBook(lunchCross, 1)).isTrue();  // 12 PM 1h is fine
        assertThat(scheduler.canBook(fivePm, 1)).isFalse();      // 5 PM is not a valid start
        assertThat(scheduler.canBook(sixPm, 1)).isFalse();       // 6 PM is the close hour
        assertThat(scheduler.canBook(FUTURE.atTime(16, 0), 2)).isTrue();  // 4-6 PM is the last allowed 2-hour window
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
        d.setTherapyType(in.thehealingpresence.enquiry.domain.TherapyType.HYPNOTHERAPY);
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
