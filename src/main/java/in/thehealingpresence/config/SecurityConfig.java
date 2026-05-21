package in.thehealingpresence.config;

import in.thehealingpresence.config.properties.AdminCredentialsProperties;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Single-role auth: one ADMIN user, in-memory.
 *
 * <p>LDAP was removed per the simplification request — the embedded UnboundID
 * server, the {@code users.ldif} fixture, and the multi-role
 * (ADMIN/STAFF/RECEPTIONIST) split are gone. Every admin-only path
 * ({@code /admin/**}, {@code /staff/**}, {@code /reception/**}) now requires
 * {@code ROLE_ADMIN}; everything else is public.
 *
 * <p>Credentials are read from {@link AdminCredentialsProperties} so the
 * username + password can be overridden via env vars ({@code ADMIN_USERNAME},
 * {@code ADMIN_PASSWORD}) in prod without touching code.
 */
@Configuration
@Profile("!test")
public class SecurityConfig {

    private final AdminCredentialsProperties adminProps;

    public SecurityConfig(AdminCredentialsProperties adminProps) {
        this.adminProps = adminProps;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Single in-memory admin user. The password is BCrypt-hashed at boot from the
     * plain-text value supplied via {@link AdminCredentialsProperties#password()};
     * never persisted in plaintext.
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.builder()
                .username(adminProps.username())
                .password(encoder.encode(adminProps.password()))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }

    /** Post-login redirect: the admin user lands on the booking-panel day grid. */
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) ->
                response.sendRedirect(request.getContextPath() + "/reception");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // JSP forwards target /WEB-INF/views/... — don't re-authorize them.
                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR, DispatcherType.INCLUDE).permitAll()
                .requestMatchers(
                        "/", "/about", "/therapy", "/training", "/accommodations",
                        "/contact", "/rent-our-space",
                        "/book-session", "/enquire-space",
                        "/login", "/login.html",
                        "/css/**", "/js/**", "/images/**", "/videos/**",
                        "/favicon.ico", "/error/**"
                ).permitAll()
                // Every authenticated surface is admin-only now — STAFF / RECEPTIONIST
                // roles were retired with the in-memory user collapse.
                .requestMatchers("/admin/**", "/staff/**", "/reception/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(successHandler())
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/?loggedOut")
                .permitAll()
            )
            // Public POST endpoints (contact / book-session / enquire-space) come from
            // the same origin; AJAX still uses CSRF via meta-tag header for admin paths.
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/contact", "/book-session", "/enquire-space")
            )
            .headers(headers -> headers
                .frameOptions(f -> f.deny())
                .contentTypeOptions(c -> {})
                .referrerPolicy(r -> r.policy(
                        org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
            );

        return http.build();
    }
}
