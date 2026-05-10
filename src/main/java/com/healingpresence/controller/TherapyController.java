package com.healingpresence.controller;

import com.healingpresence.dto.BookingFormDto;
import com.healingpresence.service.ContentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TherapyController {

    private final ContentService content;

    public TherapyController(ContentService content) {
        this.content = content;
    }

    @GetMapping("/therapy")
    public String therapy(Model model) {
        model.addAttribute("therapists", content.getTherapists());
        model.addAttribute("testimonials", content.getPublishedTestimonials());
        model.addAttribute("faqs", content.getFaqs());
        model.addAttribute("bookingForm", new BookingFormDto());
        return "pages/therapy";
    }
}
