package com.example.moyiza_be.domain.club.repository;


import com.example.moyiza_be.domain.club.entity.CreateClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreateClubRepository extends JpaRepository<CreateClub, Long> {
    Optional<CreateClub> findByOwnerIdAndFlagConfirmedIsFalse(Long ownerId);
    Optional<CreateClub> findByIdAndFlagConfirmedFalse(Long createclub_id);
}
