package in.thehealingpresence.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ContactFormDto {

    @NotBlank
    @Size(max = 60)
    private String firstName;

    @NotBlank
    @Size(max = 60)
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @NotBlank
    @Size(max = 20)
    @Pattern(regexp = "^\\+?\\d[\\d\\s-]{7,18}$", message = "must be a valid phone number")
    private String phone;

    @Size(max = 2000)
    private String message;

    @AssertTrue(message = "You must agree to the privacy policy")
    private boolean consent;

    public ContactFormDto() {
    }

    public ContactFormDto(String firstName, String lastName, String email, String phone,
                          String message, boolean consent) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.message = message;
        this.consent = consent;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isConsent() {
        return consent;
    }

    public void setConsent(boolean consent) {
        this.consent = consent;
    }
}
