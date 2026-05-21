package in.thehealingpresence.service;

import in.thehealingpresence.TestSecurityConfig;
import in.thehealingpresence.domain.BookingRequest;
import in.thehealingpresence.dto.BookingFormDto;
import in.thehealingpresence.repository.BookingRequestRepository;
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
class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRequestRepository repository;

    @MockBean
    private EmailService emailService;

    @Test
    void savePersistsBookingAndDispatchesEmailsAfterCommit() {
        BookingFormDto dto = new BookingFormDto(
                "Aadi Kapoor",
                "aadi@example.com",
                "+91 9876543210",
                "2026-06-01",
                "Hypnotherapy",
                "First-time client");

        BookingRequest saved = bookingService.save(dto);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Aadi Kapoor");
        assertThat(saved.getTherapyType()).isEqualTo("Hypnotherapy");
        assertThat(repository.findById(saved.getId())).isPresent();

        verify(emailService).notifyAdmin(any(BookingRequest.class));
        verify(emailService).sendThankYou(eq("aadi@example.com"), eq("Aadi Kapoor"), eq("session booking"));
    }
}
