package in.thehealingpresence.enquiry.pages;

import in.thehealingpresence.TestSecurityConfig;
import in.thehealingpresence.service.ContactService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContactController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;

    @Test
    void getContactReturns200() throws Exception {
        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/contact"))
                .andExpect(model().attributeExists("form"));
    }

    @Test
    void postContactWithInvalidEmailStaysOnForm() throws Exception {
        mockMvc.perform(post("/contact")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "not-an-email")
                        .param("phone", "+91 1234567890")
                        .param("message", "Hello")
                        .param("consent", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/contact"));
    }

    @Test
    void postContactWithValidDataRedirects() throws Exception {
        mockMvc.perform(post("/contact")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john@example.com")
                        .param("phone", "+91 1234567890")
                        .param("message", "Hello")
                        .param("consent", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact?ok"));
    }
}
