package in.thehealingpresence.calendar;

import in.thehealingpresence.booking.domain.Booking;
import in.thehealingpresence.calendar.domain.OAuthToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Test-profile {@link CalendarPort} that swallows every call. Wired only when
 * {@code spring.profiles.active=test} is set — the production
 * {@link GoogleCalendarAdapter} bean is excluded by the converse {@code @Profile("!test")}.
 *
 * <p>Lets {@code @SpringBootTest} contexts boot without needing Google API
 * credentials in env and without any real network call to googleapis.com.
 * Verifies via {@code Mockito.verify(noOpCalendar).pushBooking(...)} in
 * listener tests.
 */
@Component
@Profile("test")
public class NoOpCalendarAdapter implements CalendarPort {

    private static final Logger log = LoggerFactory.getLogger(NoOpCalendarAdapter.class);

    @Override
    public Optional<String> pushBooking(Booking booking) {
        log.debug("NoOpCalendarAdapter: skipping push for booking {} (test profile)", booking.id());
        return Optional.empty();
    }

    @Override
    public void deleteEvent(String eventId) {
        log.debug("NoOpCalendarAdapter: skipping delete for event {} (test profile)", eventId);
    }

    @Override
    public boolean isConfigured() {
        return false;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public String buildAuthorizationUrl() {
        throw new UnsupportedOperationException("OAuth flow is disabled under the test profile");
    }

    @Override
    public void completeAuthorization(String code) {
        throw new UnsupportedOperationException("OAuth flow is disabled under the test profile");
    }

    @Override
    public Optional<OAuthToken> getStoredToken() {
        return Optional.empty();
    }
}
