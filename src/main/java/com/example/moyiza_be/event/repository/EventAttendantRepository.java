package com.example.moyiza_be.event.repository;

import com.example.moyiza_be.event.entity.Event;
import com.example.moyiza_be.event.entity.EventAttendant;
import com.example.moyiza_be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventAttendantRepository extends JpaRepository<EventAttendant, Long> {

    EventAttendant findByEventIdAndUserId(Long eventId, Long id);
//    Optional<Object> findByEventAndUser(Event event, User user);
}
