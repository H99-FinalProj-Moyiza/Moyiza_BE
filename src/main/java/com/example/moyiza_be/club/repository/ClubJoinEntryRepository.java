package com.example.moyiza_be.club.repository;

import com.example.moyiza_be.club.entity.ClubJoinEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubJoinEntryRepository extends JpaRepository<ClubJoinEntry, Long> {
    List<ClubJoinEntry> findByClubId(Long clubId);
    Optional<ClubJoinEntry> findByUserIdAndClubId(Long id, Long clubId);
    Boolean existsByClubIdAndUserId(Long clubId, Long userId);

    void deleteByClubId(Long clubId);

    List<ClubJoinEntry> findByUserId(Long id);
}
