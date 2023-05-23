package com.example.moyiza_be.event.repository;

import com.example.moyiza_be.event.dto.EventDetailResponseDto;
import com.example.moyiza_be.event.entity.Event;
import com.example.moyiza_be.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, User> {
    // 전체조회
    List<Event> findAllByClubId(long clubId);

//    EventDetailResponseDto findById(long id);
    Optional<Event> findById(long id);

    void deleteById(long id);
}
