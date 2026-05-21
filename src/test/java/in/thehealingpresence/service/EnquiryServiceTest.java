package in.thehealingpresence.service;

import in.thehealingpresence.TestSecurityConfig;
import in.thehealingpresence.domain.SpaceEnquiry;
import in.thehealingpresence.dto.EnquiryFormDto;
import in.thehealingpresence.repository.SpaceEnquiryRepository;
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
class EnquiryServiceTest {

    @Autowired
    private EnquiryService enquiryService;

    @Autowired
    private SpaceEnquiryRepository repository;

    @MockBean
    private EmailService emailService;

    @Test
    void savePersistsEnquiryAndDispatchesEmailsAfterCommit() {
        EnquiryFormDto dto = new EnquiryFormDto(
                "Priya Iyer",
                "priya@example.com",
                "+91 9876543210",
                "Workshop",
                "2026-07-15",
                30,
                "Two-day mindfulness workshop");

        SpaceEnquiry saved = enquiryService.save(dto);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEventType()).isEqualTo("Workshop");
        assertThat(saved.getAttendees()).isEqualTo(30);
        assertThat(repository.findById(saved.getId())).isPresent();

        verify(emailService).notifyAdmin(any(SpaceEnquiry.class));
        verify(emailService).sendThankYou(eq("priya@example.com"), eq("Priya Iyer"), eq("space enquiry"));
    }
}
