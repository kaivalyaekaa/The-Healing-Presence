package in.thehealingpresence.repository;

import in.thehealingpresence.domain.ContactSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactSubmissionRepository extends JpaRepository<ContactSubmission, Long> {
}
