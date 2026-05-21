package in.thehealingpresence.controller;

import in.thehealingpresence.service.ContentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ContentService content;

    public HomeController(ContentService content) {
        this.content = content;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("therapists", content.getTherapists());
        model.addAttribute("testimonials", content.getPublishedTestimonials());
        model.addAttribute("trainings", content.getTrainings());
        model.addAttribute("faqs", content.getFaqs());
        return "pages/index";
    }
}
