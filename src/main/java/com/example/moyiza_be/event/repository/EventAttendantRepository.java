package com.example.moyiza_be.event.repository;

import com.example.moyiza_be.event.entity.EventAttendant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventAttendantRepository extends JpaRepository<EventAttendant, Long> {

    EventAttendant findByEventIdAndUserId(Long eventId, Long id);

    List<EventAttendant> findByEventId(Long eventId);
}
