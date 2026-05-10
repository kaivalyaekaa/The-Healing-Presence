package com.healingpresence.repository;

import com.healingpresence.domain.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, Long> {

    List<Faq> findAllByOrderByDisplayOrderAsc();

    List<Faq> findByCategoryOrderByDisplayOrderAsc(String category);
}
