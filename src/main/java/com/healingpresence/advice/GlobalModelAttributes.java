package com.healingpresence.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @ModelAttribute("currentUri")
    public String currentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("activeProfile")
    public String activeProfile() {
        return activeProfile;
    }

    @ModelAttribute("isDevProfile")
    public boolean isDevProfile() {
        return "dev".equalsIgnoreCase(activeProfile);
    }
}
