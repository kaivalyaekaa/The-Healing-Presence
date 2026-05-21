package in.thehealingpresence.config.properties;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Single in-memory admin user — username + plain-text password, both injected
 * via {@code app.admin.username} / {@code app.admin.password} so prod can
 * override with env vars ({@code ADMIN_USERNAME}, {@code ADMIN_PASSWORD}) and
 * dev gets a sensible default from {@code application.yml}.
 *
 * <p>The password is hashed at boot by {@link in.thehealingpresence.config.SecurityConfig}
 * — never stored or compared in plaintext.
 */
@Validated
@ConfigurationProperties(prefix = "app.admin")
public record AdminCredentialsProperties(
        @NotBlank(message = "app.admin.username must not be blank") String username,
        @NotBlank(message = "app.admin.password must not be blank") String password
) {
}
