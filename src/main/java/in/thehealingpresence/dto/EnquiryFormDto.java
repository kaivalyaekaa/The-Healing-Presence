package in.thehealingpresence.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EnquiryFormDto {

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
    @Size(max = 60)
    private String eventType;

    @Size(max = 20)
    private String preferredDate;

    @Min(value = 1, message = "must be at least 1")
    @Max(value = 1000, message = "must be at most 1000")
    private Integer attendees;

    @Size(max = 2000)
    private String message;

    public EnquiryFormDto() {
    }

    public EnquiryFormDto(String name, String email, String phone, String eventType,
                          String preferredDate, Integer attendees, String message) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.eventType = eventType;
        this.preferredDate = preferredDate;
        this.attendees = attendees;
        this.message = message;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getPreferredDate() { return preferredDate; }
    public void setPreferredDate(String preferredDate) { this.preferredDate = preferredDate; }

    public Integer getAttendees() { return attendees; }
    public void setAttendees(Integer attendees) { this.attendees = attendees; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
