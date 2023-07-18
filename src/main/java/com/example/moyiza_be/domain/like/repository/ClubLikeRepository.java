package com.example.moyiza_be.domain.like.repository;

import com.example.moyiza_be.domain.like.entity.ClubLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubLikeRepository extends JpaRepository<ClubLike, Long> {
    Boolean existsByUserIdAndClubId(Long userId, Long clubId);
    void deleteByUserIdAndClubId(Long userId, Long clubId);

    List<ClubLike> findAllByUserId(Long userId);

}
