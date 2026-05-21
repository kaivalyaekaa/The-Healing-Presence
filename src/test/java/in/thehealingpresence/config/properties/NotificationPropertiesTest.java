package in.thehealingpresence.config.properties;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.validation.BindValidationException;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Proves the {@code @NotBlank} validation on {@link NotificationProperties}
 * surfaces at startup rather than at first email send. A blank
 * {@code app.notifications.to} fails fast with a {@link BindValidationException}
 * naming the offending field.
 */
class NotificationPropertiesTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withUserConfiguration(NotificationPropertiesEnabler.class);

    @Test
    void validBindingSucceeds() {
        runner.withPropertyValues(
                        "app.notifications.to=info@example.com",
                        "app.notifications.from=noreply@example.com")
                .run(ctx -> {
                    assertThat(ctx).hasNotFailed();
                    NotificationProperties props = ctx.getBean(NotificationProperties.class);
                    assertThat(props.to()).isEqualTo("info@example.com");
                    assertThat(props.from()).isEqualTo("noreply@example.com");
                });
    }

    @Test
    void blankToFailsAtStartup() {
        runner.withPropertyValues(
                        "app.notifications.to=",
                        "app.notifications.from=noreply@example.com")
                .run(ctx -> {
                    assertThat(ctx).hasFailed();
                    // Cause chain bubbles a BindValidationException with a property-path including 'to'.
                    assertThat(ctx.getStartupFailure())
                            .rootCause()
                            .isInstanceOf(BindValidationException.class)
                            .hasMessageContaining("to");
                });
    }

    @org.springframework.boot.context.properties.EnableConfigurationProperties(NotificationProperties.class)
    static class NotificationPropertiesEnabler {
    }
}
