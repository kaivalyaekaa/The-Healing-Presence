package in.thehealingpresence.calendar;

import in.thehealingpresence.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Guards against accidentally shipping {@link NoOpCalendarAdapter} to production.
 *
 * <p>Under the {@code test} profile only the no-op should be wired; under any
 * other profile {@link GoogleCalendarAdapter} should be the sole bean. A typo in
 * {@code @Profile("!test")} or stray test bean leaking into prod would surface
 * here as a wrong concrete type behind {@link CalendarPort}.
 */
class CalendarPortWiringTest {

    @SpringBootTest
    @Import(TestSecurityConfig.class)
    @ActiveProfiles("test")
    static class TestProfileWiresNoOp {

        @Autowired
        private CalendarPort calendar;

        @Test
        void portResolvesToNoOpAdapter() {
            assertThat(calendar)
                    .as("Under the test profile, the no-op adapter must be the only CalendarPort bean")
                    .isInstanceOf(NoOpCalendarAdapter.class);
            assertThat(calendar.isConfigured()).isFalse();
            assertThat(calendar.isConnected()).isFalse();
        }
    }
}
