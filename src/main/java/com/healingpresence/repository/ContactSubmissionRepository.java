package com.healingpresence.repository;

import com.healingpresence.domain.ContactSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactSubmissionRepository extends JpaRepository<ContactSubmission, Long> {
}
