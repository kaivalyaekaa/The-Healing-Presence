package in.thehealingpresence.scheduling;

import java.util.List;

/**
 * The office schedule constants for the receptionist scheduler.
 * <p>
 * Office hours: 10 AM – 6 PM with a hard lunch block 1–2 PM. Six valid
 * booking-start hours (three per half) with the 4 PM start able to extend to
 * 6 PM as a 2-hour booking — giving the "5–6 PM block" only reachable as
 * the cascade hour of a 4 PM 2-hour booking, never as a direct booking start.
 *
 * <pre>
 * Hour:  10 11 12 13 14 15 16 17 18
 *        |  |  |  L  |  |  |  c  end
 *        valid START hours: [10, 11, 12, 14, 15, 16]   (six)
 *        hour 17 (5 PM)   : reachable only as cascade-hour of a 16:00 2h booking
 * </pre>
 */
public final class OfficeHours {

    private OfficeHours() {}

    /** First hour-of-day that may be booked (10:00 AM). */
    public static final int OPEN_HOUR = 10;

    /**
     * Hour-of-day the office closes (18 = 6:00 PM).
     * A 2-hour booking at 16:00 ends at 18:00 (the close hour itself, allowed by the
     * scheduler's "end &lt;= CLOSE_HOUR" check).
     */
    public static final int CLOSE_HOUR = 18;

    /** Lunch break starts (13 = 1:00 PM). */
    public static final int LUNCH_START = 13;

    /** Lunch break ends (14 = 2:00 PM). */
    public static final int LUNCH_END = 14;

    /**
     * The hours that may be the START of a booking.
     * Lunch is excluded; the close hour is excluded; 17 (5 PM) is excluded — the
     * 5–6 PM slot exists only as the cascade hour of a 16:00 two-hour booking.
     */
    public static List<Integer> validStartHours() {
        return List.of(10, 11, 12, 14, 15, 16);
    }
}
