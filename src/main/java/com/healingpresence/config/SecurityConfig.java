package com.healingpresence.config;

import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.web.SecurityFilterChain;

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
                        "/css/**", "/js/**", "/images/**", "/icons/**", "/videos/**",
                        "/favicon.ico", "/error/**"
                ).permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/staff/**").hasAnyRole("ADMIN", "STAFF")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/staff", true)
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
