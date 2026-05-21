package in.thehealingpresence.enquiry.pages;

import in.thehealingpresence.dto.EnquiryFormDto;
import in.thehealingpresence.service.EnquiryService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
public class SpaceEnquiryController {

    private final EnquiryService enquiryService;

    public SpaceEnquiryController(EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    @PostMapping(value = "/enquire-space", produces = MediaType.TEXT_HTML_VALUE)
    public String enquireSpaceClassic(@Valid @ModelAttribute("enquiryForm") EnquiryFormDto enquiryForm,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "pages/rent-our-space";
        }
        enquiryService.save(enquiryForm);
        redirectAttributes.addFlashAttribute("toast", "Your enquiry has been submitted successfully!");
        return "redirect:/rent-our-space";
    }

    @PostMapping(value = "/enquire-space", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> enquireSpaceAjax(@Valid @RequestBody EnquiryFormDto enquiryForm,
                                                                BindingResult bindingResult) {
        Map<String, Object> body = new HashMap<>();
        if (bindingResult.hasErrors()) {
            body.put("ok", false);
            body.put("errors", fieldErrors(bindingResult));
            return ResponseEntity.badRequest().body(body);
        }
        enquiryService.save(enquiryForm);
        body.put("ok", true);
        body.put("message", "Your enquiry has been submitted successfully!");
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
