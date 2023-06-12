package com.example.moyiza_be.oneday.repository;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.oneday.dto.OneDayNearByResponseDto;
import com.example.moyiza_be.oneday.entity.OneDay;
import com.example.moyiza_be.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OneDayRepository extends JpaRepository<OneDay, User> {

    Optional<OneDay> findById(Long oneDayId);
    Optional<OneDay> findByIdAndDeletedFalse(Long onedayId);

    void deleteById(Long oneDayId);


    Page<OneDay> findByCategoryAndDeletedFalseAndOneDayTitleContaining(Pageable pageable, CategoryEnum category, String q);
    Page<OneDay> findByCategoryAndDeletedFalse(Pageable pageable, CategoryEnum category);

    Page<OneDay> findByDeletedFalseAndOneDayTitleContaining(Pageable pageable, String q);

    Page<OneDay> findAllByDeletedFalse(Pageable pageable);

    boolean existsByIdAndDeletedFalseAndOwnerIdEquals(Long oneDayId, Long userId);

//    @Query("SELECT o FROM OneDay o WHERE (6371 * acos(cos(radians(:nowLatitude)) * cos(radians(o.oneDayLatitude)) * cos(radians(o.oneDayLongitude) - radians(:nowLongitude)) + sin(radians(:nowLatitude)) * sin(radians(o.oneDayLatitude)))) <= 10")
//    List<OneDay> findAllByOneDayLatitudeAndOneDayLongitude(@Param("nowLatitude") double nowLatitude, @Param("nowLongitude") double nowLongitude);
    @Query("SELECT o, (6371 * acos(cos(radians(:nowLatitude)) * cos(radians(o.oneDayLatitude)) * cos(radians(o.oneDayLongitude) - radians(:nowLongitude)) + sin(radians(:nowLatitude)) * sin(radians(o.oneDayLatitude)))) AS distance " +
            "FROM OneDay o " +
            "WHERE (6371 * acos(cos(radians(:nowLatitude)) * cos(radians(o.oneDayLatitude)) * cos(radians(o.oneDayLongitude) - radians(:nowLongitude)) + sin(radians(:nowLatitude)) * sin(radians(o.oneDayLatitude)))) <= 10 " +
            "ORDER BY distance")
    List<Object[]> findNearByOneDays(@Param("nowLatitude") double nowLatitude, @Param("nowLongitude") double nowLongitude);

    List<OneDay> findAllByOneDayStartTime(LocalDate today, LocalTime nowAfter30);
}
