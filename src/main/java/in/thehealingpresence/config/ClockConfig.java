package in.thehealingpresence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

/**
 * Provides a single {@link Clock} bean fixed to Asia/Kolkata (IST) — the only
 * timezone The Healing Presence operates in. Inject {@code Clock} into any
 * service or controller that needs the current instant; tests substitute a
 * fixed clock to make time-dependent behaviour deterministic.
 *
 * <p>The scheduler ({@code SlotSchedulerService}) already pulls its clock via
 * constructor injection. This bean replaces the {@code Clock.systemDefaultZone()}
 * default and ensures every consumer sees IST.
 */
@Configuration
public class ClockConfig {

    public static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    @Bean
    public Clock clock() {
        return Clock.system(IST);
    }
}
