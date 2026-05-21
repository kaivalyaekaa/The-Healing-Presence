package in.thehealingpresence.notification;

import in.thehealingpresence.booking.domain.Booking;
import in.thehealingpresence.booking.event.BookingCreatedEvent;
import in.thehealingpresence.calendar.CalendarPort;
import in.thehealingpresence.enquiry.domain.TherapyType;
import in.thehealingpresence.repository.BookingRequestRepository;
import in.thehealingpresence.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookingNotificationListenerTest {

    private EmailService emailService;
    private CalendarPort calendar;
    private BookingRequestRepository bookingRepository;
    private BookingNotificationListener listener;

    @BeforeEach
    void setUp() {
        emailService = mock(EmailService.class);
        calendar = mock(CalendarPort.class);
        bookingRepository = mock(BookingRequestRepository.class);
        listener = new BookingNotificationListener(emailService, calendar, bookingRepository);
    }

    private static Booking sampleBooking(Long id) {
        LocalDateTime start = LocalDateTime.of(2026, 6, 1, 10, 0);
        return Booking.newConfirmed("Aadi Kapoor", "aadi@example.com", "+91 9876543210",
                start, start.plusHours(2), 2, TherapyType.CRYSTAL_HEALING, "Bring quartz")
                .withGoogleEventId(null)
                .withGoogleEventId(null); // ensure no id baked in
    }

    @Test
    void firesBothEmailAndCalendarPush() {
        Booking b = sampleBooking(7L);
        when(calendar.pushBooking(any())).thenReturn(Optional.empty());

        listener.onBookingCreated(new BookingCreatedEvent(b));

        verify(emailService).notifyAdmin(b);
        verify(emailService).sendThankYou(eq("aadi@example.com"), eq("Aadi Kapoor"), eq("session booking"));
        verify(calendar).pushBooking(b);
    }

    @Test
    void calendarFailureDoesNotPreventEmail() {
        Booking b = sampleBooking(8L);
        when(calendar.pushBooking(any())).thenThrow(new RuntimeException("Google API down"));

        // Listener must not propagate the exception (best-effort side-effects).
        listener.onBookingCreated(new BookingCreatedEvent(b));

        verify(emailService).notifyAdmin(b);
        verify(emailService).sendThankYou(any(), any(), any());
        verify(calendar).pushBooking(b);
        // No save attempt because no event id came back.
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void successfulCalendarPushPersistsEventIdOnBooking() {
        // Listener needs to find the BookingRequest via its id to persist the googleEventId.
        in.thehealingpresence.domain.BookingRequest persisted = new in.thehealingpresence.domain.BookingRequest();
        persisted.setId(9L);
        when(bookingRepository.findById(9L)).thenReturn(Optional.of(persisted));
        when(calendar.pushBooking(any())).thenReturn(Optional.of("event-xyz-789"));

        Booking b = Booking.newConfirmed("Aadi Kapoor", "aadi@example.com", "+91 9876543210",
                LocalDateTime.of(2026, 6, 1, 10, 0), LocalDateTime.of(2026, 6, 1, 11, 0),
                1, TherapyType.HYPNOTHERAPY, null);
        // Inject the id via reflection-free path: construct a Booking with id=9
        Booking withId = new Booking(9L, b.clientName(), b.clientEmail(), b.clientPhone(),
                b.slotStart(), b.slotEnd(), b.durationHours(), b.therapyType(), b.notes(),
                b.status(), null, null);

        listener.onBookingCreated(new BookingCreatedEvent(withId));

        ArgumentCaptor<in.thehealingpresence.domain.BookingRequest> captor =
                ArgumentCaptor.forClass(in.thehealingpresence.domain.BookingRequest.class);
        verify(bookingRepository).save(captor.capture());
        assertThat(captor.getValue().getGoogleEventId()).isEqualTo("event-xyz-789");
    }
}
