package in.thehealingpresence.service;

import in.thehealingpresence.TestSecurityConfig;
import in.thehealingpresence.domain.ContactSubmission;
import in.thehealingpresence.dto.ContactFormDto;
import in.thehealingpresence.repository.ContactSubmissionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class ContactServiceTest {

    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactSubmissionRepository repository;

    @MockBean
    private EmailService emailService;

    @Test
    void savePersistsSubmissionAndDispatchesEmailsAfterCommit() {
        ContactFormDto dto = new ContactFormDto("Jane", "Doe", "jane@example.com",
                "+91 9876543210", "Test message", true);

        ContactSubmission saved = contactService.save(dto);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getFirstName()).isEqualTo("Jane");
        assertThat(saved.getEmail()).isEqualTo("jane@example.com");
        assertThat(repository.findById(saved.getId())).isPresent();

        // The TransactionalEventListener with phase=AFTER_COMMIT fires once the
        // outer @Transactional in ContactService commits.
        verify(emailService).notifyAdmin(any(ContactSubmission.class));
        verify(emailService).sendThankYou(eq("jane@example.com"), eq("Jane"), eq("message"));
    }
}
