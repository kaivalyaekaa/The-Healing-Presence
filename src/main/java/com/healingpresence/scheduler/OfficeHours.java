package com.healingpresence.scheduler;

import java.util.List;

/**
 * The office schedule constants for the receptionist scheduler.
 * <p>
 * Default: 10 AM – 7 PM with a hard lunch block 1–2 PM, giving 8 bookable hourly starts.
 * Adjust here to change office hours globally; SlotSchedulerService picks up the values.
 *
 * <pre>
 * Hour:  10 11 12 13 14 15 16 17 18 19
 *        |  |  |  L  |  |  |  |  |  end
 *        bookable slots: [10, 11, 12, 14, 15, 16, 17, 18]
 * </pre>
 */
public final class OfficeHours {

    private OfficeHours() {}

    /** First hour-of-day that may be booked (10:00 AM). */
    public static final int OPEN_HOUR = 10;

    /** Hour-of-day the office closes (19 = 7:00 PM). 1-hour booking at 18 ends at 19. */
    public static final int CLOSE_HOUR = 19;

    /** Lunch break starts (13 = 1:00 PM). */
    public static final int LUNCH_START = 13;

    /** Lunch break ends (14 = 2:00 PM). */
    public static final int LUNCH_END = 14;

    /**
     * The hours that may be the START of a booking.
     * Lunch hour is excluded; the close hour is excluded (no booking at 19:00 because
     * even a 1-hour slot would end at 20:00, after close).
     */
    public static List<Integer> validStartHours() {
        return List.of(10, 11, 12, 14, 15, 16, 17, 18);
    }
}
