package in.thehealingpresence.controller;

import in.thehealingpresence.service.ContentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TrainingController {

    private final ContentService content;

    public TrainingController(ContentService content) {
        this.content = content;
    }

    @GetMapping("/training")
    public String training(Model model) {
        model.addAttribute("trainings", content.getTrainings());
        return "pages/training";
    }
}
