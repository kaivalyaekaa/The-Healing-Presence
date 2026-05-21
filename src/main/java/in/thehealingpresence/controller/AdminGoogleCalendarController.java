package in.thehealingpresence.controller;

import in.thehealingpresence.domain.OAuthToken;
import in.thehealingpresence.service.GoogleCalendarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

/**
 * Admin-only endpoints for the one-time Google Calendar OAuth dance.
 * Gated by {@code /admin/**} → {@code hasRole('ADMIN')} in SecurityConfig.
 */
@Controller
@RequestMapping("/admin/google-calendar")
public class AdminGoogleCalendarController {

    private final GoogleCalendarService googleCalendarService;

    public AdminGoogleCalendarController(GoogleCalendarService googleCalendarService) {
        this.googleCalendarService = googleCalendarService;
    }

    @GetMapping
    public String status(Model model,
                         @RequestParam(name = "connected", required = false) Boolean justConnected,
                         @RequestParam(name = "error", required = false) String error) {
        model.addAttribute("configured", googleCalendarService.isConfigured());
        model.addAttribute("connected", googleCalendarService.isConnected());
        model.addAttribute("justConnected", Boolean.TRUE.equals(justConnected));
        model.addAttribute("error", error);

        Optional<OAuthToken> token = googleCalendarService.getStoredToken();
        token.ifPresent(t -> {
            model.addAttribute("connectedAt", t.getUpdatedAt() != null ? t.getUpdatedAt() : t.getCreatedAt());
            model.addAttribute("scope", t.getScope());
        });

        return "pages/admin/google-calendar";
    }

    @GetMapping("/connect")
    public RedirectView connect() {
        String url = googleCalendarService.buildAuthorizationUrl();
        return new RedirectView(url);
    }

    @GetMapping("/callback")
    public RedirectView callback(@RequestParam(name = "code", required = false) String code,
                                 @RequestParam(name = "error", required = false) String error) {
        if (error != null && !error.isBlank()) {
            return new RedirectView("/admin/google-calendar?error=" + error);
        }
        if (code == null || code.isBlank()) {
            return new RedirectView("/admin/google-calendar?error=missing_code");
        }
        try {
            googleCalendarService.exchangeCode(code);
            return new RedirectView("/admin/google-calendar?connected=true");
        } catch (Exception e) {
            return new RedirectView("/admin/google-calendar?error=exchange_failed");
        }
    }
}
