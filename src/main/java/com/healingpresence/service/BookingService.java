package com.healingpresence.service;

import com.healingpresence.domain.BookingRequest;
import com.healingpresence.dto.BookingFormDto;
import com.healingpresence.event.BookingSubmittedEvent;
import com.healingpresence.repository.BookingRequestRepository;
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
