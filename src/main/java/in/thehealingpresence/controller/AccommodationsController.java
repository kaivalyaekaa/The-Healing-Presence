package in.thehealingpresence.controller;

import in.thehealingpresence.dto.EnquiryFormDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccommodationsController {

    @GetMapping("/accommodations")
    public String accommodations(Model model) {
        if (!model.containsAttribute("enquiryForm")) {
            EnquiryFormDto dto = new EnquiryFormDto();
            dto.setEventType("Stay at Vasudha");
            model.addAttribute("enquiryForm", dto);
        }
        return "pages/accommodations";
    }
}
