package in.thehealingpresence.repository;

import in.thehealingpresence.domain.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, Long> {

    List<Faq> findAllByOrderByDisplayOrderAsc();

    List<Faq> findByCategoryOrderByDisplayOrderAsc(String category);
}
