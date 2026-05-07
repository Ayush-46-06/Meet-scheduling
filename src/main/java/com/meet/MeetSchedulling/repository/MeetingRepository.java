package com.meet.MeetSchedulling.repository;

import com.meet.MeetSchedulling.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByUserEmail(String userEmail);
    boolean existsByStartTimeLessThanAndEndTimeGreaterThan(
            LocalDateTime endTime,
            LocalDateTime startTime
    );
}