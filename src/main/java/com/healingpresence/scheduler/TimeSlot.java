package com.healingpresence.scheduler;

import com.healingpresence.domain.BookingRequest;

import java.time.LocalDateTime;

/**
 * One hour-slot in the receptionist day-grid view.
 *
 * <p>Plain class (not a record) so JSP EL can resolve {@code slot.status} etc. via
 * standard JavaBean getters — record-accessor resolution in JSP EL is brittle.
 */
public class TimeSlot {

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

    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }
    public int getDurationHours() { return durationHours; }
    public SlotStatus getStatus() { return status; }
    public BookingRequest getBooking() { return booking; }

    /** Record-style accessors kept for Java-side callers and tests. */
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
}
