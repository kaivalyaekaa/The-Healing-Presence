package com.healingpresence.service;

import com.healingpresence.domain.ContactSubmission;
import com.healingpresence.dto.ContactFormDto;
import com.healingpresence.event.ContactSubmittedEvent;
import com.healingpresence.repository.ContactSubmissionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContactService {

    private final ContactSubmissionRepository contactSubmissionRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ContactService(ContactSubmissionRepository contactSubmissionRepository,
                          ApplicationEventPublisher eventPublisher) {
        this.contactSubmissionRepository = contactSubmissionRepository;
        this.eventPublisher = eventPublisher;
    }

    public ContactSubmission save(ContactFormDto dto) {
        ContactSubmission submission = new ContactSubmission();
        submission.setFirstName(dto.getFirstName());
        submission.setLastName(dto.getLastName());
        submission.setEmail(dto.getEmail());
        submission.setPhone(dto.getPhone());
        submission.setMessage(dto.getMessage());
        submission.setConsentGiven(dto.isConsent());

        ContactSubmission saved = contactSubmissionRepository.save(submission);
        eventPublisher.publishEvent(new ContactSubmittedEvent(saved));
        return saved;
    }
}
