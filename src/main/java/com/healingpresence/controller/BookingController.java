package com.healingpresence.controller;

import com.healingpresence.dto.BookingFormDto;
import com.healingpresence.service.BookingService;
import com.healingpresence.service.ContentService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final ContentService content;

    public BookingController(BookingService bookingService, ContentService content) {
        this.bookingService = bookingService;
        this.content = content;
    }

    @PostMapping(value = "/book-session", produces = MediaType.TEXT_HTML_VALUE)
    public String bookSessionClassic(@Valid @ModelAttribute("bookingForm") BookingFormDto bookingForm,
                                     BindingResult bindingResult,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Re-render /therapy with the surrounding content intact alongside the form errors.
            model.addAttribute("therapists", content.getTherapists());
            model.addAttribute("testimonials", content.getPublishedTestimonials());
            model.addAttribute("faqs", content.getFaqs());
            return "pages/therapy";
        }
        bookingService.save(bookingForm);
        redirectAttributes.addFlashAttribute("toast", "Your booking request has been submitted successfully!");
        return "redirect:/therapy";
    }

    @PostMapping(value = "/book-session", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> bookSessionAjax(@Valid @RequestBody BookingFormDto bookingForm,
                                                               BindingResult bindingResult) {
        Map<String, Object> body = new HashMap<>();
        if (bindingResult.hasErrors()) {
            body.put("ok", false);
            body.put("errors", fieldErrors(bindingResult));
            return ResponseEntity.badRequest().body(body);
        }
        bookingService.save(bookingForm);
        body.put("ok", true);
        body.put("message", "Your booking request has been submitted successfully!");
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
