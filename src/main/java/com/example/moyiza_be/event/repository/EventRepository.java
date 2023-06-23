package com.example.moyiza_be.event.repository;

import com.example.moyiza_be.event.entity.Event;
import com.example.moyiza_be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, User> {
    // ReadAll
    List<Event> findAllByClubId(long clubId);

//    EventDetailResponseDto findById(long id);
    Optional<Event> findById(long id);

    void deleteById(long id);

    void deleteByClubId(Long clubId);
    Optional<Event> findByIdAndDeletedIsFalse(long eventId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Event e WHERE e.deleted=true AND e.modifiedAt < :targetDate")
    void cleanUpDeletedEvents(@Param("targetDate")LocalDateTime targetDate);
}
