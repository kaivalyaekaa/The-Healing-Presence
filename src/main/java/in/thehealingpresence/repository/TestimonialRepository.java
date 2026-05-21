package in.thehealingpresence.repository;

import in.thehealingpresence.domain.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestimonialRepository extends JpaRepository<Testimonial, Long> {

    List<Testimonial> findByPublishedTrueOrderByCreatedAtDesc();
}
