package com.healingpresence.repository;

import com.healingpresence.domain.TrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Long> {

    List<TrainingProgram> findAllByOrderByDisplayOrderAsc();
}
