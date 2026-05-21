package in.thehealingpresence.controller;

import in.thehealingpresence.dto.EnquiryFormDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RentSpaceController {

    @GetMapping("/rent-our-space")
    public String rentSpace(Model model) {
        model.addAttribute("enquiryForm", new EnquiryFormDto());
        return "pages/rent-our-space";
    }
}
