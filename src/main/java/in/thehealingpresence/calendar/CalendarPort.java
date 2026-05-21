package in.thehealingpresence.calendar;

import in.thehealingpresence.booking.domain.Booking;
import in.thehealingpresence.calendar.domain.OAuthToken;

import java.util.Optional;

/**
 * Bounded-context port for pushing receptionist bookings to an external calendar
 * (currently Google Calendar) without coupling the rest of the application to a
 * specific provider's SDK.
 *
 * <p>The {@link in.thehealingpresence.notification.BookingNotificationListener}
 * depends on this interface. Implementations:
 * <ul>
 *   <li>{@link GoogleCalendarAdapter} — production: real Google Calendar API.</li>
 *   <li>{@link NoOpCalendarAdapter} — test/dev fallback: returns empty, logs only.</li>
 * </ul>
 *
 * <p>All methods are best-effort. A failure to push or delete must never
 * roll back the originating booking transaction; implementations should swallow
 * exceptions and surface them via logs.
 */
public interface CalendarPort {

    /**
     * Push a booking to the calendar.
     * @return the provider's event id (to persist on the booking) or empty
     *         if the adapter is not configured/connected, or the push failed.
     */
    Optional<String> pushBooking(Booking booking);

    /** Best-effort delete. No-op if {@code eventId} is null/blank or the adapter is not configured. */
    void deleteEvent(String eventId);

    /** True if the adapter has credentials (env vars present in dev, OAuth client configured in prod). */
    boolean isConfigured();

    /** True if {@link #isConfigured()} AND a refresh token has been stored via {@link #completeAuthorization(String)}. */
    boolean isConnected();

    /** URL to redirect the admin to for the one-time OAuth consent flow. */
    String buildAuthorizationUrl();

    /**
     * Exchange the OAuth authorization code returned to the callback for a
     * refresh token, and persist it.
     */
    void completeAuthorization(String code);

    /** The stored refresh-token row, if any (for the admin status page). */
    Optional<OAuthToken> getStoredToken();
}
