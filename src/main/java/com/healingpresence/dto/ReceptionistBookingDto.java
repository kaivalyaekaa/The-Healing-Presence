package com.healingpresence.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Form-backing bean for the receptionist /reception/new booking form.
 * Mirrors the field discipline of {@link BookingFormDto} (the public-form DTO) but
 * adds the typed slot information that the SlotSchedulerService needs.
 */
public class ReceptionistBookingDto {

    @NotBlank
    @Size(max = 120)
    private String clientName;

    @NotBlank
    @Email
    @Size(max = 150)
    private String clientEmail;

    @NotBlank
    @Size(max = 20)
    @Pattern(regexp = "^\\+?\\d[\\d\\s-]{7,18}$", message = "must be a valid phone number")
    private String clientPhone;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate slotDate;

    /** 10, 11, 12, 14, 15, 16, 17, or 18 (lunch 13 excluded; scheduler validates). */
    @NotNull
    @Min(10)
    @Max(18)
    private Integer slotHour;

    /** 1 or 2 hours. Scheduler rejects 2h at 12 PM (lunch crossover) and at 6 PM (after-close). */
    @NotNull
    @Min(1)
    @Max(2)
    private Integer durationHours;

    @NotBlank
    @Size(max = 60)
    private String therapyType;

    @Size(max = 2000)
    private String notes;

    public ReceptionistBookingDto() {
    }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getClientEmail() { return clientEmail; }
    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }

    public String getClientPhone() { return clientPhone; }
    public void setClientPhone(String clientPhone) { this.clientPhone = clientPhone; }

    public LocalDate getSlotDate() { return slotDate; }
    public void setSlotDate(LocalDate slotDate) { this.slotDate = slotDate; }

    public Integer getSlotHour() { return slotHour; }
    public void setSlotHour(Integer slotHour) { this.slotHour = slotHour; }

    public Integer getDurationHours() { return durationHours; }
    public void setDurationHours(Integer durationHours) { this.durationHours = durationHours; }

    public String getTherapyType() { return therapyType; }
    public void setTherapyType(String therapyType) { this.therapyType = therapyType; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
