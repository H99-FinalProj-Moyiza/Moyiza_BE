package com.example.moyiza_be.domain.oneday.repository;

import com.example.moyiza_be.domain.oneday.entity.OneDayAttendant;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OneDayAttendantRepository extends JpaRepository<OneDayAttendant, Long> {

    List<OneDayAttendant> findAllByOneDayId(Long oneDayId);

    @Query("SELECT a.oneDayId AS oneDayId, u.name AS userName, u.nickname AS userNickName, u.profileImage AS userImage FROM OneDayAttendant a JOIN users u ON a.userId = u.id  WHERE a.oneDayId = :oneDayId")
    List<OneDayAttendant> findAttendantsByOneDayId(@Param("oneDayId") Long oneDayId);

    Optional<OneDayAttendant> findByOneDayIdAndUserId(Long oneDayId, Long id);

    boolean existsByOneDayIdAndUserId(Long oneDayId, Long id);

    List<OneDayAttendant> findByUserId(Long userId);
}
