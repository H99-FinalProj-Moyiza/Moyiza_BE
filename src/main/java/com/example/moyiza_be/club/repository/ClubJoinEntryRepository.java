package com.example.moyiza_be.club.repository;

import com.example.moyiza_be.club.entity.ClubJoinEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubJoinEntryRepository extends JpaRepository<ClubJoinEntry, Long> {
    List<ClubJoinEntry> findByClubId(Long clubId);
    ClubJoinEntry findByUserIdAndClubId(Long id, Long clubId);
    Boolean existsByClubIdAndUserId(Long clubId, Long userId);

    void deleteByClubId(Long clubId);
}
