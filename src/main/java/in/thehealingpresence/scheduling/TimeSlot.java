package in.thehealingpresence.scheduling;

import in.thehealingpresence.domain.BookingRequest;

import java.time.LocalDateTime;

/**
 * One hour-slot in the receptionist day-grid view.
 *
 * <p>Record. JSP EL resolves both the record accessors ({@code slot.start},
 * {@code slot.status}) — JSP EL property resolution calls any zero-arg
 * method whose name matches the property — and the explicit
 * {@link #getLabel()} / {@link #isClickable()} helpers below.
 */
public record TimeSlot(
        LocalDateTime start,
        LocalDateTime end,
        int durationHours,
        SlotStatus status,
        BookingRequest booking
) {

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
