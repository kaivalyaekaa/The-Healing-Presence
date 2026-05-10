package com.healingpresence.repository;

import com.healingpresence.domain.Therapist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TherapistRepository extends JpaRepository<Therapist, Long> {

    List<Therapist> findAllByOrderByDisplayOrderAsc();
}
