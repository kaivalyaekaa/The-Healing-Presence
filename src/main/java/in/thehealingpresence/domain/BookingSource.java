package in.thehealingpresence.domain;

/**
 * Where a {@link BookingRequest} originated.
 * <ul>
 *   <li>{@link #PUBLIC_FORM} — the public-facing /therapy AJAX form (free-text preferredDate, no calendar push)</li>
 *   <li>{@link #RECEPTIONIST} — the staff-only /reception admin panel (typed slot, pushed to Google Calendar)</li>
 * </ul>
 */
public enum BookingSource {
    PUBLIC_FORM,
    RECEPTIONIST
}
