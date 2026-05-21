package com.healingpresence.config;

import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Set;

@Configuration
@Profile("!test")
public class SecurityConfig {

    @Value("${spring.ldap.embedded.url}")
    private String ldapUrl;

    @Value("${spring.ldap.embedded.base-dn}")
    private String baseDn;

    @Bean
    public BaseLdapPathContextSource contextSource() {
        LdapContextSource source = new LdapContextSource();
        source.setUrl(ldapUrl);
        source.setBase(baseDn);
        source.setAnonymousReadOnly(true);
        source.afterPropertiesSet();
        return source;
    }

    /**
     * Route post-login redirect based on role:
     *   - RECEPTIONIST (without ADMIN/STAFF) → /reception (the booking panel)
     *   - everyone else (ADMIN, STAFF) → /staff
     */
    @Bean
    public AuthenticationSuccessHandler roleBasedSuccessHandler() {
        return (request, response, authentication) -> {
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
            boolean isReceptionOnly = roles.contains("ROLE_RECEPTIONIST")
                    && !roles.contains("ROLE_ADMIN")
                    && !roles.contains("ROLE_STAFF");
            response.sendRedirect(request.getContextPath() + (isReceptionOnly ? "/reception" : "/staff"));
        };
    }

    @Bean
    public LdapAuthenticationProvider ldapAuthenticationProvider(BaseLdapPathContextSource contextSource) {
        FilterBasedLdapUserSearch userSearch =
                new FilterBasedLdapUserSearch("ou=people", "(uid={0})", contextSource);

        BindAuthenticator bindAuthenticator = new BindAuthenticator(contextSource);
        bindAuthenticator.setUserSearch(userSearch);

        DefaultLdapAuthoritiesPopulator authorities =
                new DefaultLdapAuthoritiesPopulator(contextSource, "ou=groups");
        authorities.setGroupSearchFilter("(uniqueMember={0})");
        authorities.setRolePrefix("ROLE_");
        authorities.setConvertToUpperCase(true);

        return new LdapAuthenticationProvider(bindAuthenticator, authorities);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   LdapAuthenticationProvider ldapProvider) throws Exception {
        http
            .authenticationProvider(ldapProvider)
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
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/staff/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers("/reception/**").hasAnyRole("ADMIN", "RECEPTIONIST")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(roleBasedSuccessHandler())
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/?loggedOut")
                .permitAll()
            )
            // Public form posts (contact / book-session / enquire-space) come from the same origin,
            // but to keep AJAX simple we expose CSRF as a cookie and skip it for the public POSTs.
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
