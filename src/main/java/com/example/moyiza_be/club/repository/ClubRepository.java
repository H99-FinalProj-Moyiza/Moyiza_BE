package com.example.moyiza_be.club.repository;

import com.example.moyiza_be.club.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findByCategoryAndTitleContaining(String category, String q);

    List<Club> findByCategory(String category);

    List<Club> findByTitleContaining(String q);
}
