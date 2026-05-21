package in.thehealingpresence.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        @RequestParam(required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("loginError", "Invalid username or password.");
        }
        if (logout != null) {
            model.addAttribute("loginInfo", "You have been signed out.");
        }
        return "pages/login";
    }

    @GetMapping("/staff")
    public String staff(Authentication authentication, Model model) {
        model.addAttribute("displayName", authentication != null ? authentication.getName() : "");
        model.addAttribute("authorities", authentication != null ? authentication.getAuthorities() : null);
        return "pages/staff";
    }
}
