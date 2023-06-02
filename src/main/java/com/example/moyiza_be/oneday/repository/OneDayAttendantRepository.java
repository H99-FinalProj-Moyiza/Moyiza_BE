package com.example.moyiza_be.oneday.repository;

import com.example.moyiza_be.oneday.entity.OneDayAttendant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OneDayAttendantRepository extends JpaRepository<OneDayAttendant, Long> {

    List<OneDayAttendant> findByOneDayId(long oneDayId);

    OneDayAttendant findByOneDayIdAndUserId(Long oneDayId, Long id);
}
