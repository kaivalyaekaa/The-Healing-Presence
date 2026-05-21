package in.thehealingpresence.config.properties;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Typed bindings for {@code app.notifications.*} — sender + recipient addresses
 * used by the email listener. Both fields are required (application.yml provides
 * sensible defaults so boot only fails if a malformed override is set).
 */
@Validated
@ConfigurationProperties(prefix = "app.notifications")
public record NotificationProperties(
        @NotBlank(message = "app.notifications.to must not be blank") String to,
        @NotBlank(message = "app.notifications.from must not be blank") String from
) {
}
