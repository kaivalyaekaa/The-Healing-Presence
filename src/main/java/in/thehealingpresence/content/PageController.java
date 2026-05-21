package in.thehealingpresence.content;

import in.thehealingpresence.dto.BookingFormDto;
import in.thehealingpresence.dto.EnquiryFormDto;
import in.thehealingpresence.service.ContentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Public marketing pages — home, about, therapy, training, accommodations,
 * rent-our-space. All six handlers share the same content-loading pattern
 * (a {@link ContentService} call + a form-DTO if the page hosts one), so
 * collapsing them into a single controller eliminates six near-empty classes
 * without losing any readability.
 *
 * <p>Post handlers for these pages (e.g. {@code /book-session},
 * {@code /enquire-space}, {@code /contact}) live in the {@code enquiry/}
 * slice — page rendering is a separate concern from form processing.
 */
@Controller
public class PageController {

    private final ContentService content;

    public PageController(ContentService content) {
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

    @GetMapping("/about")
    public String about() {
        return "pages/about";
    }

    @GetMapping("/therapy")
    public String therapy(Model model) {
        model.addAttribute("therapists", content.getTherapists());
        model.addAttribute("testimonials", content.getPublishedTestimonials());
        model.addAttribute("faqs", content.getFaqs());
        model.addAttribute("bookingForm", new BookingFormDto());
        return "pages/therapy";
    }

    @GetMapping("/training")
    public String training(Model model) {
        model.addAttribute("trainings", content.getTrainings());
        return "pages/training";
    }

    @GetMapping("/accommodations")
    public String accommodations(Model model) {
        if (!model.containsAttribute("enquiryForm")) {
            EnquiryFormDto dto = new EnquiryFormDto();
            dto.setEventType("Stay at Vasudha");
            model.addAttribute("enquiryForm", dto);
        }
        return "pages/accommodations";
    }

    @GetMapping("/rent-our-space")
    public String rentSpace(Model model) {
        model.addAttribute("enquiryForm", new EnquiryFormDto());
        return "pages/rent-our-space";
    }
}
