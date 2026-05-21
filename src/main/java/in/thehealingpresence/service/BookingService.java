package in.thehealingpresence.service;

import in.thehealingpresence.domain.BookingRequest;
import in.thehealingpresence.dto.BookingFormDto;
import in.thehealingpresence.event.BookingSubmittedEvent;
import in.thehealingpresence.repository.BookingRequestRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookingService {

    private final BookingRequestRepository bookingRequestRepository;
    private final ApplicationEventPublisher eventPublisher;

    public BookingService(BookingRequestRepository bookingRequestRepository,
                          ApplicationEventPublisher eventPublisher) {
        this.bookingRequestRepository = bookingRequestRepository;
        this.eventPublisher = eventPublisher;
    }

    public BookingRequest save(BookingFormDto dto) {
        BookingRequest request = new BookingRequest();
        request.setName(dto.getName());
        request.setEmail(dto.getEmail());
        request.setPhone(dto.getPhone());
        request.setTherapyType(dto.getTherapyType());
        request.setPreferredDate(dto.getPreferredDate());
        request.setNotes(dto.getNotes());

        BookingRequest saved = bookingRequestRepository.save(request);
        eventPublisher.publishEvent(new BookingSubmittedEvent(saved));
        return saved;
    }
}
