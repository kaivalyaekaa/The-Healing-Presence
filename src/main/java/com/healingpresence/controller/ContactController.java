package com.healingpresence.controller;

import com.healingpresence.dto.ContactFormDto;
import com.healingpresence.service.ContactService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contact")
    public String contactForm(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new ContactFormDto());
        }
        return "pages/contact";
    }

    @PostMapping(value = "/contact", produces = MediaType.TEXT_HTML_VALUE)
    public String submitContactClassic(@Valid @ModelAttribute("form") ContactFormDto form,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "pages/contact";
        }
        contactService.save(form);
        redirectAttributes.addFlashAttribute("toast", "Thank you for your message! We will get back to you soon.");
        return "redirect:/contact?ok";
    }

    @PostMapping(value = "/contact", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> submitContactAjax(
            @Valid @org.springframework.web.bind.annotation.RequestBody ContactFormDto form,
            BindingResult bindingResult,
            HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        if (bindingResult.hasErrors()) {
            body.put("ok", false);
            body.put("errors", fieldErrors(bindingResult));
            return ResponseEntity.badRequest().body(body);
        }
        contactService.save(form);
        body.put("ok", true);
        body.put("message", "Thank you for your message! We will get back to you soon.");
        return ResponseEntity.ok(body);
    }

    private Map<String, String> fieldErrors(BindingResult bindingResult) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : bindingResult.getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Invalid value");
        }
        return errors;
    }
}
