package in.thehealingpresence.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking_requests")
public class BookingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Instant createdAt;

    private String name;

    private String email;

    private String phone;

    private String preferredDate;

    private String therapyType;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SubmissionStatus status = SubmissionStatus.NEW;

    // ===== Receptionist-scheduler fields (nullable for back-compat with public-form bookings) =====

    /** Start of the reserved slot. NULL for public-form bookings (which use the free-text preferredDate). */
    private LocalDateTime slotStart;

    /** End of the reserved slot (slotStart + durationHours). NULL for public-form bookings. */
    private LocalDateTime slotEnd;

    /** Duration in hours: 1 or 2. NULL for public-form bookings. */
    private Integer durationHours;

    /** Where the booking originated. Defaults to PUBLIC_FORM for back-compat. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingSource bookingSource = BookingSource.PUBLIC_FORM;

    /** Google Calendar event ID, populated after successful push. NULL until pushed (or for public-form). */
    private String googleEventId;

    public BookingRequest() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

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

    public SubmissionStatus getStatus() { return status; }
    public void setStatus(SubmissionStatus status) { this.status = status; }

    public LocalDateTime getSlotStart() { return slotStart; }
    public void setSlotStart(LocalDateTime slotStart) { this.slotStart = slotStart; }

    public LocalDateTime getSlotEnd() { return slotEnd; }
    public void setSlotEnd(LocalDateTime slotEnd) { this.slotEnd = slotEnd; }

    public Integer getDurationHours() { return durationHours; }
    public void setDurationHours(Integer durationHours) { this.durationHours = durationHours; }

    public BookingSource getBookingSource() { return bookingSource; }
    public void setBookingSource(BookingSource bookingSource) { this.bookingSource = bookingSource; }

    public String getGoogleEventId() { return googleEventId; }
    public void setGoogleEventId(String googleEventId) { this.googleEventId = googleEventId; }
}
