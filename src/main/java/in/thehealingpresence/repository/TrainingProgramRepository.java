package in.thehealingpresence.repository;

import in.thehealingpresence.domain.TrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Long> {

    List<TrainingProgram> findAllByOrderByDisplayOrderAsc();
}
