package in.thehealingpresence.enquiry;

import in.thehealingpresence.domain.BookingRequest;
import in.thehealingpresence.domain.BookingSource;
import in.thehealingpresence.domain.SubmissionStatus;
import in.thehealingpresence.enquiry.domain.Enquiry;
import in.thehealingpresence.enquiry.domain.EnquiryStatus;
import in.thehealingpresence.repository.BookingRequestRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Facade over the persistence-layer {@link BookingRequestRepository} that
 * exposes only enquiry-shaped reads and writes. The rest of the application
 * depends on this — never on {@link BookingRequestRepository} directly.
 *
 * <p>Filters every read by {@code bookingSource == PUBLIC_FORM} so receptionist
 * rows never accidentally surface as enquiries.
 */
@Component
public class EnquiryRepository {

    private final BookingRequestRepository delegate;

    public EnquiryRepository(BookingRequestRepository delegate) {
        this.delegate = delegate;
    }

    /** Persists a domain {@link Enquiry}; returns the saved domain object with id populated. */
    public Enquiry save(Enquiry enquiry) {
        BookingRequest persisted = delegate.save(EnquiryMapper.toPersistence(enquiry));
        return EnquiryMapper.toDomain(persisted);
    }

    public Optional<Enquiry> findById(Long id) {
        return delegate.findById(id)
                .filter(b -> b.getBookingSource() == BookingSource.PUBLIC_FORM)
                .map(EnquiryMapper::toDomain);
    }

    public List<Enquiry> findByStatus(EnquiryStatus status) {
        SubmissionStatus persistenceStatus = switch (status) {
            case NEW -> SubmissionStatus.NEW;
            case READ -> SubmissionStatus.READ;
            case REPLIED -> SubmissionStatus.REPLIED;
        };
        return delegate.findByStatusOrderBySlotStartAsc(persistenceStatus).stream()
                .filter(b -> b.getBookingSource() == BookingSource.PUBLIC_FORM)
                .map(EnquiryMapper::toDomain)
                .toList();
    }
}
