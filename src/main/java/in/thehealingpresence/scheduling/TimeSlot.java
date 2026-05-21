package in.thehealingpresence.scheduling;

import in.thehealingpresence.domain.BookingRequest;

import java.time.LocalDateTime;

/**
 * One hour-slot in the receptionist day-grid view.
 *
 * <p>Explicit class with both classic JavaBean getters ({@code getStatus()})
 * AND record-style accessors ({@code status()}). The dual API exists because
 * JSP EL property resolution (e.g. {@code ${slot.status}}) is reliable across
 * Tomcat versions only with classic getters, while Java-side callers and
 * tests prefer the shorter record-style accessor.
 */
public final class TimeSlot {

    private final LocalDateTime start;
    private final LocalDateTime end;
    private final int durationHours;
    private final SlotStatus status;
    private final BookingRequest booking;

    public TimeSlot(LocalDateTime start, LocalDateTime end, int durationHours,
                    SlotStatus status, BookingRequest booking) {
        this.start = start;
        this.end = end;
        this.durationHours = durationHours;
        this.status = status;
        this.booking = booking;
    }

    // Classic JavaBean getters — what JSP EL `${slot.xxx}` resolves to.
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }
    public int getDurationHours() { return durationHours; }
    public SlotStatus getStatus() { return status; }
    public BookingRequest getBooking() { return booking; }

    // Record-style accessors — what Java callers/tests use.
    public LocalDateTime start() { return start; }
    public LocalDateTime end() { return end; }
    public int durationHours() { return durationHours; }
    public SlotStatus status() { return status; }
    public BookingRequest booking() { return booking; }

    /** Human-readable display label like "10:00 AM". */
    public String getLabel() {
        int hour = start.getHour();
        int displayHour = (hour == 0) ? 12 : (hour > 12 ? hour - 12 : hour);
        String suffix = hour < 12 ? "AM" : "PM";
        return String.format("%d:00 %s", displayHour, suffix);
    }

    /** Alias for JSP: ${slot.label()} works because EL discovers it as a method. */
    public String label() { return getLabel(); }

    /** True if the receptionist can click this slot to create a new booking. */
    public boolean isClickable() {
        return status == SlotStatus.AVAILABLE;
    }
}
