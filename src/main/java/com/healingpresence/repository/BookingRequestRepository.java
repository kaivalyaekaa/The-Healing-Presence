package com.healingpresence.repository;

import com.healingpresence.domain.BookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRequestRepository extends JpaRepository<BookingRequest, Long> {
}
