package com.example.moyiza_be.club.repository;

import com.example.moyiza_be.club.entity.ClubImageUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubImageUrlRepository extends JpaRepository<ClubImageUrl, Long> {
    List<ClubImageUrl> findAllByClubId(Long createclub_id);

}
