package in.thehealingpresence.config.properties;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Typed bindings for the {@code spring.ldap.embedded.*} block — the embedded
 * UnboundID directory used in dev. Both fields are required and have defaults
 * in application.yml.
 *
 * <p>Bound from the {@code spring.ldap.embedded} prefix rather than {@code app.ldap}
 * so it composes with Spring Boot's existing embedded-LDAP auto-configuration
 * without duplicating keys.
 */
@Validated
@ConfigurationProperties(prefix = "spring.ldap.embedded")
public record LdapProperties(
        @NotBlank(message = "spring.ldap.embedded.url must not be blank") String url,
        @NotBlank(message = "spring.ldap.embedded.base-dn must not be blank") String baseDn
) {
}
