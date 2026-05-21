package in.thehealingpresence.service;

import in.thehealingpresence.domain.Faq;
import in.thehealingpresence.domain.Therapist;
import in.thehealingpresence.domain.TrainingProgram;
import in.thehealingpresence.domain.Testimonial;
import in.thehealingpresence.repository.FaqRepository;
import in.thehealingpresence.repository.TestimonialRepository;
import in.thehealingpresence.repository.TherapistRepository;
import in.thehealingpresence.repository.TrainingProgramRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentService {

    private final TherapistRepository therapistRepository;
    private final TrainingProgramRepository trainingProgramRepository;
    private final TestimonialRepository testimonialRepository;
    private final FaqRepository faqRepository;

    public ContentService(TherapistRepository therapistRepository,
                          TrainingProgramRepository trainingProgramRepository,
                          TestimonialRepository testimonialRepository,
                          FaqRepository faqRepository) {
        this.therapistRepository = therapistRepository;
        this.trainingProgramRepository = trainingProgramRepository;
        this.testimonialRepository = testimonialRepository;
        this.faqRepository = faqRepository;
    }

    @Cacheable("therapists")
    public List<Therapist> getTherapists() {
        return therapistRepository.findAllByOrderByDisplayOrderAsc();
    }

    @Cacheable("trainings")
    public List<TrainingProgram> getTrainings() {
        return trainingProgramRepository.findAllByOrderByDisplayOrderAsc();
    }

    @Cacheable("testimonials")
    public List<Testimonial> getPublishedTestimonials() {
        return testimonialRepository.findByPublishedTrueOrderByCreatedAtDesc();
    }

    @Cacheable("faqs")
    public List<Faq> getFaqs() {
        return faqRepository.findAllByOrderByDisplayOrderAsc();
    }
}
