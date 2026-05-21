package in.thehealingpresence.repository;

import in.thehealingpresence.domain.BookingRequest;
import in.thehealingpresence.domain.SubmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRequestRepository extends JpaRepository<BookingRequest, Long> {

    /**
     * Day-grid query: all bookings whose slot starts inside [start, end).
     * Order by slot start so the receptionist UI renders chronologically.
     */
    List<BookingRequest> findBySlotStartBetweenOrderBySlotStartAsc(LocalDateTime start, LocalDateTime end);

    /**
     * Overlap check used by the slot scheduler: returns any non-cancelled bookings
     * that intersect the window [start, end). Used to guard against double-bookings.
     */
    @Query("""
        SELECT b FROM BookingRequest b
        WHERE b.slotStart < :end
          AND b.slotEnd > :start
          AND b.status <> in.thehealingpresence.domain.SubmissionStatus.CANCELLED
        ORDER BY b.slotStart ASC
    """)
    List<BookingRequest> findOverlapping(@Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end);

    /** Status filter for the admin "today's confirmed bookings" view. */
    List<BookingRequest> findByStatusOrderBySlotStartAsc(SubmissionStatus status);
}
