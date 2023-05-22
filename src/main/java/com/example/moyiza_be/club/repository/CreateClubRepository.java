package com.example.moyiza_be.club.repository;


import com.example.moyiza_be.club.entity.CreateClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreateClubRepository extends JpaRepository<CreateClub, Long> {
}
