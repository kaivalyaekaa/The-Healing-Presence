package in.thehealingpresence.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;

/**
 * Cross-cutting JSP model attributes — current URI for active-nav highlighting,
 * plus the active Spring profile (used by the dev-credentials banner on the
 * login page). Reads the profile from {@link Environment} instead of a string
 * {@code @Value} so multi-profile configurations (e.g. {@code dev,rebuild})
 * are handled correctly.
 */
@ControllerAdvice
public class GlobalModelAttributes {

    private final Environment environment;

    public GlobalModelAttributes(Environment environment) {
        this.environment = environment;
    }

    @ModelAttribute("currentUri")
    public String currentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("activeProfile")
    public String activeProfile() {
        String[] active = environment.getActiveProfiles();
        return active.length == 0 ? "default" : String.join(",", active);
    }

    @ModelAttribute("isDevProfile")
    public boolean isDevProfile() {
        return Arrays.asList(environment.getActiveProfiles()).contains("dev");
    }
}
