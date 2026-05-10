package com.healingpresence.repository;

import com.healingpresence.domain.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestimonialRepository extends JpaRepository<Testimonial, Long> {

    List<Testimonial> findByPublishedTrueOrderByCreatedAtDesc();
}
