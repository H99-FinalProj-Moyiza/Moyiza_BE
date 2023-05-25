package com.example.moyiza_be.mypage.repository;

import com.example.moyiza_be.club.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyPageRepository extends JpaRepository<Club, Long> {

    List<Club> findAllByOwnerId(Long id);
}
