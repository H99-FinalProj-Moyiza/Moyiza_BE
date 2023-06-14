package com.example.moyiza_be.like.repository;

import com.example.moyiza_be.like.entity.ClubLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubLikeRepository extends JpaRepository<ClubLike, Long> {
    Boolean existsByUserIdAndClubId(Long userId, Long clubId);
    void deleteByUserIdAndClubId(Long userId, Long clubId);

}
