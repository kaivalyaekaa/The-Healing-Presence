package in.thehealingpresence.service;

import in.thehealingpresence.domain.SpaceEnquiry;
import in.thehealingpresence.dto.EnquiryFormDto;
import in.thehealingpresence.event.EnquirySubmittedEvent;
import in.thehealingpresence.repository.SpaceEnquiryRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EnquiryService {

    private final SpaceEnquiryRepository spaceEnquiryRepository;
    private final ApplicationEventPublisher eventPublisher;

    public EnquiryService(SpaceEnquiryRepository spaceEnquiryRepository,
                          ApplicationEventPublisher eventPublisher) {
        this.spaceEnquiryRepository = spaceEnquiryRepository;
        this.eventPublisher = eventPublisher;
    }

    public SpaceEnquiry save(EnquiryFormDto dto) {
        SpaceEnquiry enquiry = new SpaceEnquiry();
        enquiry.setName(dto.getName());
        enquiry.setEmail(dto.getEmail());
        enquiry.setPhone(dto.getPhone());
        enquiry.setEventType(dto.getEventType());
        enquiry.setPreferredDate(dto.getPreferredDate());
        enquiry.setAttendees(dto.getAttendees());
        enquiry.setMessage(dto.getMessage());

        SpaceEnquiry saved = spaceEnquiryRepository.save(enquiry);
        eventPublisher.publishEvent(new EnquirySubmittedEvent(saved));
        return saved;
    }
}
