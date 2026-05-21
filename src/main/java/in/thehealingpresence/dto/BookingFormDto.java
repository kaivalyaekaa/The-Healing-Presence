package in.thehealingpresence.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class BookingFormDto {

    @NotBlank
    @Size(max = 120)
    private String name;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @NotBlank
    @Size(max = 20)
    @Pattern(regexp = "^\\+?\\d[\\d\\s-]{7,18}$", message = "must be a valid phone number")
    private String phone;

    @NotBlank
    @Size(max = 20)
    private String preferredDate;

    @NotBlank
    @Size(max = 60)
    private String therapyType;

    @Size(max = 2000)
    private String notes;

    public BookingFormDto() {
    }

    public BookingFormDto(String name, String email, String phone, String preferredDate,
                          String therapyType, String notes) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.preferredDate = preferredDate;
        this.therapyType = therapyType;
        this.notes = notes;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPreferredDate() { return preferredDate; }
    public void setPreferredDate(String preferredDate) { this.preferredDate = preferredDate; }

    public String getTherapyType() { return therapyType; }
    public void setTherapyType(String therapyType) { this.therapyType = therapyType; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
