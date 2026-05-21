package in.thehealingpresence.booking;

import in.thehealingpresence.booking.domain.Booking;
import in.thehealingpresence.booking.domain.BookingStatus;
import in.thehealingpresence.domain.BookingRequest;
import in.thehealingpresence.domain.BookingSource;
import in.thehealingpresence.domain.SubmissionStatus;
import in.thehealingpresence.repository.BookingRequestRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Facade over the persistence-layer {@link BookingRequestRepository} that
 * exposes only booking-shaped reads and writes. The scheduler, the receptionist
 * controller, and the notification listener all depend on this — never on
 * {@link BookingRequestRepository} directly.
 *
 * <p>Filters every read by {@code bookingSource == RECEPTIONIST} so public-form
 * enquiries never accidentally surface as bookings.
 */
@Component
public class BookingRepository {

    private final BookingRequestRepository delegate;

    public BookingRepository(BookingRequestRepository delegate) {
        this.delegate = delegate;
    }

    /** Persists a domain {@link Booking}; returns the saved domain object with id populated. */
    public Booking save(Booking booking) {
        BookingRequest persisted = delegate.save(BookingMapper.toPersistence(booking));
        return BookingMapper.toDomain(persisted);
    }

    public Optional<Booking> findById(Long id) {
        return delegate.findById(id)
                .filter(r -> r.getBookingSource() == BookingSource.RECEPTIONIST)
                .map(BookingMapper::toDomain);
    }

    /** Day-grid query: bookings whose slot starts inside [start, end). */
    public List<Booking> findBySlotStartBetween(LocalDateTime start, LocalDateTime end) {
        return delegate.findBySlotStartBetweenOrderBySlotStartAsc(start, end).stream()
                .filter(r -> r.getBookingSource() == BookingSource.RECEPTIONIST)
                .map(BookingMapper::toDomain)
                .toList();
    }

    /** Overlap check: non-cancelled bookings that intersect [start, end). */
    public List<Booking> findOverlapping(LocalDateTime start, LocalDateTime end) {
        return delegate.findOverlapping(start, end).stream()
                .filter(r -> r.getBookingSource() == BookingSource.RECEPTIONIST)
                .map(BookingMapper::toDomain)
                .toList();
    }

    public List<Booking> findByStatus(BookingStatus status) {
        SubmissionStatus persistenceStatus = switch (status) {
            case CONFIRMED -> SubmissionStatus.CONFIRMED;
            case CANCELLED -> SubmissionStatus.CANCELLED;
        };
        return delegate.findByStatusOrderBySlotStartAsc(persistenceStatus).stream()
                .filter(r -> r.getBookingSource() == BookingSource.RECEPTIONIST)
                .map(BookingMapper::toDomain)
                .toList();
    }
}
