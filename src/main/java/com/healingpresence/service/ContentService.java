package com.healingpresence.service;

import com.healingpresence.domain.Faq;
import com.healingpresence.domain.Therapist;
import com.healingpresence.domain.TrainingProgram;
import com.healingpresence.domain.Testimonial;
import com.healingpresence.repository.FaqRepository;
import com.healingpresence.repository.TestimonialRepository;
import com.healingpresence.repository.TherapistRepository;
import com.healingpresence.repository.TrainingProgramRepository;
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
