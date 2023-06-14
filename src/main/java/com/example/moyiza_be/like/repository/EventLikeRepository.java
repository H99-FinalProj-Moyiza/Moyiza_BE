package com.example.moyiza_be.like.repository;

import com.example.moyiza_be.like.entity.EventLike;
import com.example.moyiza_be.like.entity.OnedayLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLikeRepository extends JpaRepository<EventLike, Long> {

    Boolean existsByUserIdAndEventId(Long userId, Long eventId);

    void deleteByUserIdAndEventId(Long userId, Long eventId);
}
