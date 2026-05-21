package in.thehealingpresence.booking.pages;

import in.thehealingpresence.domain.BookingRequest;
import in.thehealingpresence.dto.ReceptionistBookingDto;
import in.thehealingpresence.repository.BookingRequestRepository;
import in.thehealingpresence.calendar.CalendarPort;
import in.thehealingpresence.scheduling.OfficeHours;
import in.thehealingpresence.scheduling.SlotSchedulerService;
import in.thehealingpresence.scheduling.TimeSlot;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Receptionist admin panel routes. All gated by {@code /reception/**} →
 * {@code hasRole('ADMIN')} in {@link in.thehealingpresence.config.SecurityConfig}.
 */
@Controller
@RequestMapping("/reception")
public class ReceptionController {

    private final SlotSchedulerService scheduler;
    private final BookingRequestRepository bookingRepository;
    private final CalendarPort calendar;

    public ReceptionController(SlotSchedulerService scheduler,
                               BookingRequestRepository bookingRepository,
                               CalendarPort calendar) {
        this.scheduler = scheduler;
        this.bookingRepository = bookingRepository;
        this.calendar = calendar;
    }

    // ------------------------------------------------------------ day-grid view --

    @GetMapping({"", "/", "/day"})
    public String dashboard(
            @RequestParam(name = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {
        LocalDate target = (date != null) ? date : LocalDate.now();
        List<TimeSlot> slots = scheduler.getDaySlots(target);

        model.addAttribute("date", target);
        model.addAttribute("prevDate", target.minusDays(1));
        model.addAttribute("nextDate", target.plusDays(1));
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("slots", slots);
        model.addAttribute("openHour", OfficeHours.OPEN_HOUR);
        model.addAttribute("closeHour", OfficeHours.CLOSE_HOUR);
        return "pages/reception/dashboard";
    }

    // ------------------------------------------------------------ new-booking form --

    @GetMapping("/new")
    public String newBookingForm(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("hour") Integer hour,
            @RequestParam(name = "duration", required = false, defaultValue = "1") Integer duration,
            Model model) {

        if (!model.containsAttribute("receptionistBooking")) {
            ReceptionistBookingDto dto = new ReceptionistBookingDto();
            dto.setSlotDate(date);
            dto.setSlotHour(hour);
            dto.setDurationHours(duration);
            model.addAttribute("receptionistBooking", dto);
        }
        model.addAttribute("date", date);
        model.addAttribute("hour", hour);
        // Disable 2h option if the cascade slot is busy.
        boolean twoHourPossible = scheduler.canBook(date.atTime(hour, 0), 2);
        model.addAttribute("twoHourPossible", twoHourPossible);
        return "pages/reception/booking-form";
    }

    @PostMapping(value = "/new", produces = MediaType.TEXT_HTML_VALUE)
    public String submitBookingClassic(
            @Valid @ModelAttribute("receptionistBooking") ReceptionistBookingDto dto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            populateFormModel(dto, model);
            return "pages/reception/booking-form";
        }
        try {
            BookingRequest saved = scheduler.createReceptionistBooking(dto);
            redirectAttributes.addFlashAttribute("toast",
                    "Booked " + saved.getName() + " for " + saved.getSlotStart().toLocalTime()
                            + " (" + saved.getDurationHours() + "h).");
            return "redirect:/reception?date=" + dto.getSlotDate();
        } catch (IllegalArgumentException e) {
            bindingResult.reject("slot.invalid", e.getMessage());
            populateFormModel(dto, model);
            return "pages/reception/booking-form";
        }
    }

    /**
     * Repopulate the model attributes the booking-form JSP needs on re-render.
     * Recomputes {@code twoHourPossible} from live state instead of hard-coding
     * (fix G6 — a concurrent booking could have just taken the cascade slot).
     */
    private void populateFormModel(ReceptionistBookingDto dto, Model model) {
        model.addAttribute("date", dto.getSlotDate());
        model.addAttribute("hour", dto.getSlotHour());
        boolean twoHourPossible = (dto.getSlotDate() != null && dto.getSlotHour() != null)
                && scheduler.canBook(dto.getSlotDate().atTime(dto.getSlotHour(), 0), 2);
        model.addAttribute("twoHourPossible", twoHourPossible);
    }

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> submitBookingAjax(
            @Valid @RequestBody ReceptionistBookingDto dto,
            BindingResult bindingResult) {
        Map<String, Object> body = new HashMap<>();
        if (bindingResult.hasErrors()) {
            body.put("ok", false);
            body.put("errors", fieldErrors(bindingResult));
            return ResponseEntity.badRequest().body(body);
        }
        try {
            BookingRequest saved = scheduler.createReceptionistBooking(dto);
            body.put("ok", true);
            body.put("message", "Booked " + saved.getName() + " — slot reserved.");
            body.put("id", saved.getId());
            return ResponseEntity.ok(body);
        } catch (IllegalArgumentException e) {
            body.put("ok", false);
            body.put("errors", Map.of("slot", e.getMessage()));
            return ResponseEntity.badRequest().body(body);
        }
    }

    // ------------------------------------------------------------ booking detail / cancel --

    @GetMapping("/booking/{id}")
    public String bookingDetail(@PathVariable Long id, Model model) {
        BookingRequest booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking " + id + " not found"));
        model.addAttribute("booking", booking);
        return "pages/reception/booking-detail";
    }

    @PostMapping("/booking/{id}/cancel")
    public String cancelBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        scheduler.cancel(id).ifPresent(b -> {
            redirectAttributes.addFlashAttribute("toast",
                    "Cancelled booking for " + b.getName() + ".");
            // Best-effort: also remove the matching Google Calendar event.
            if (b.getGoogleEventId() != null && !b.getGoogleEventId().isBlank()) {
                calendar.deleteEvent(b.getGoogleEventId());
            }
        });
        BookingRequest b = bookingRepository.findById(id).orElse(null);
        LocalDate date = (b != null && b.getSlotStart() != null)
                ? b.getSlotStart().toLocalDate() : LocalDate.now();
        return "redirect:/reception?date=" + date;
    }

    // ------------------------------------------------------------ helpers --

    private Map<String, String> fieldErrors(BindingResult bindingResult) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : bindingResult.getFieldErrors()) {
            errors.put(fe.getField(),
                    fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Invalid value");
        }
        return errors;
    }
}
