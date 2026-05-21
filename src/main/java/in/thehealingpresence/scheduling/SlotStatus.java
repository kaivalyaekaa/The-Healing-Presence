package in.thehealingpresence.scheduling;

/**
 * State of a single hour-slot in the receptionist day-grid view.
 * Extracted from {@link TimeSlot} into its own file for cleaner imports
 * (e.g. {@code import …scheduling.SlotStatus} vs nested type access).
 */
public enum SlotStatus {
    /** Slot is free; clickable to create a booking. */
    AVAILABLE,
    /** A booking starts at this hour. */
    BOOKED,
    /** Auto-blocked because the previous hour has a 2-hour booking running through this hour. */
    BLOCKED_BY_CASCADE,
    /** Lunch break (1–2 PM) — never bookable. */
    LUNCH,
    /** Slot is in the past — read-only. */
    PAST
}
