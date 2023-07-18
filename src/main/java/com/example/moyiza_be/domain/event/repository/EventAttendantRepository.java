package com.example.moyiza_be.domain.event.repository;

import com.example.moyiza_be.domain.event.entity.EventAttendant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventAttendantRepository extends JpaRepository<EventAttendant, Long> {

    EventAttendant findByEventIdAndUserId(Long eventId, Long id);

    Boolean existsByUserIdAndEventId(Long userId, Long eventId);

    List<EventAttendant> findByEventId(Long eventId);
}
