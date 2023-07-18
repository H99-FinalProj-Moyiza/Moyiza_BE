package com.example.moyiza_be.domain.oneday.repository;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.domain.oneday.entity.OneDay;
import com.example.moyiza_be.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OneDayRepository extends JpaRepository<OneDay, User> {

    Optional<OneDay> findById(Long oneDayId);

    List<OneDay> findAllById(Long oneDayIds);
    Optional<OneDay> findByIdAndDeletedFalse(Long onedayId);

    void deleteById(Long oneDayId);


    Page<OneDay> findByCategoryAndDeletedFalseAndOneDayTitleContaining(Pageable pageable, CategoryEnum category, String q);
    Page<OneDay> findByCategoryAndDeletedFalse(Pageable pageable, CategoryEnum category);

    Page<OneDay> findByDeletedFalseAndOneDayTitleContaining(Pageable pageable, String q);

    Page<OneDay> findAllByDeletedFalse(Pageable pageable);

    List<OneDay> findAllByOwnerId(Long userId);


    boolean existsByIdAndDeletedFalseAndOwnerIdEquals(Long oneDayId, Long userId);

    @Query("SELECT o, (6371 * acos(cos(radians(:nowLatitude)) * cos(radians(o.oneDayLatitude)) * cos(radians(o.oneDayLongitude) - radians(:nowLongitude)) + sin(radians(:nowLatitude)) * sin(radians(o.oneDayLatitude)))) AS distance " +
            "FROM OneDay o " +
            "WHERE (6371 * acos(cos(radians(:nowLatitude)) * cos(radians(o.oneDayLatitude)) * cos(radians(o.oneDayLongitude) - radians(:nowLongitude)) + sin(radians(:nowLatitude)) * sin(radians(o.oneDayLatitude)))) <= 10 " +
            "AND o.id NOT IN :blackOneDayIdList " +
            "AND o.deleted = false " +
            "AND o.oneDayStartTime > :now " +
            "ORDER BY distance")
    List<Object[]> findNearByOneDaysFilteredBlackList(@Param("nowLatitude") double nowLatitude,
                                                      @Param("nowLongitude") double nowLongitude,
                                                      @Param("blackOneDayIdList") List<Long> blackOneDayIdList,
                                                      @Param("now") LocalDateTime now);

    @Query("SELECT o, (6371 * acos(cos(radians(:nowLatitude)) * cos(radians(o.oneDayLatitude)) * cos(radians(o.oneDayLongitude) - radians(:nowLongitude)) + sin(radians(:nowLatitude)) * sin(radians(o.oneDayLatitude)))) AS distance " +
            "FROM OneDay o " +
            "WHERE (6371 * acos(cos(radians(:nowLatitude)) * cos(radians(o.oneDayLatitude)) * cos(radians(o.oneDayLongitude) - radians(:nowLongitude)) + sin(radians(:nowLatitude)) * sin(radians(o.oneDayLatitude)))) <= 10 " +
            "AND o.deleted = false " +
            "AND o.oneDayStartTime > :now " +
            "ORDER BY distance")
    List<Object[]> findNearByOneDays(@Param("nowLatitude") double nowLatitude,
                                     @Param("nowLongitude") double nowLongitude,
                                     @Param("now") LocalDateTime now);

    List<OneDay> findAllByDeletedFalseAndOneDayStartTimeAfterOrderByOneDayStartTimeAsc(LocalDateTime now);

    List<OneDay> findAllByDeletedFalseAndOneDayStartTimeAfterOrderByNumLikesDesc(LocalDateTime now);

    @Transactional
    @Modifying
    @Query("DELETE FROM OneDay o WHERE o.deleted=true AND o.modifiedAt < :targetDate")
    void cleanUpDeletedOneDays(@Param("targetDate")LocalDateTime targetDate);

}
