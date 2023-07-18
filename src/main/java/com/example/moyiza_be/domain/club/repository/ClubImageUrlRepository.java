package com.example.moyiza_be.domain.club.repository;

import com.example.moyiza_be.domain.club.entity.ClubImageUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubImageUrlRepository extends JpaRepository<ClubImageUrl, Long> {
    List<ClubImageUrl> findAllByClubId(Long club_id);

    List<ClubImageUrl> findAllByCreateClubId(Long createclub_id);
    Optional<ClubImageUrl> findFirstByClubId(Long clubId);
    void deleteByClubId(Long clubId);

    void deleteByImageUrl(String imageUrl);

}
