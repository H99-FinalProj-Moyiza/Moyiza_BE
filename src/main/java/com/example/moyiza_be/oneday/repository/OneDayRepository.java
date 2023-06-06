package com.example.moyiza_be.oneday.repository;

import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.oneday.entity.OneDay;
import com.example.moyiza_be.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OneDayRepository extends JpaRepository<OneDay, User> {

    Optional<OneDay> findById(Long oneDayId);

    void deleteById(Long oneDayId);


    Page<OneDay> findByCategoryAndDeletedFalseAndOneDayTitleContaining(Pageable pageable, CategoryEnum category, String q);
    Page<OneDay> findByCategoryAndDeletedFalse(Pageable pageable, CategoryEnum category);

    Page<OneDay> findByDeletedFalseAndOneDayTitleContaining(Pageable pageable, String q);

    Page<OneDay> findAllByDeletedFalse(Pageable pageable);

    boolean existsByIdAndDeletedFalseAndOwnerIdEquals(Long oneDayId, Long userId);

    //경도 위도 순
//    @Query(value = "SELECT o, ST_Distance_Sphere(:location, POINT(o.oneDayLongitude, o.oneDayLatitude)) AS distance " +
//            "FROM OneDay o " +
//            "WHERE ST_Distance_Sphere(:location, POINT(o.oneDayLongitude, o.oneDayLatitude)) <= 1000 " +
//            "ORDER BY distance")
//    List<OneDay> findAroundOneDayList(@Param("location") String location);

    @Query("SELECT o FROM OneDay o WHERE (6371 * acos(cos(radians(:nowLatitude)) * cos(radians(o.oneDayLatitude)) * cos(radians(o.oneDayLongitude) - radians(:nowLongitude)) + sin(radians(:nowLatitude)) * sin(radians(o.oneDayLatitude)))) <= 10")
    List<OneDay> findAllByOneDayLatitudeAndOneDayLongitude(@Param("nowLatitude") double nowLatitude, @Param("nowLongitude") double nowLongitude);


//    @Query(value = "SELECT o FROM OneDay o " +
//            "WHERE o.oneDayLatitude <= :maxY " +
//            "AND o.oneDayLatitude >= :minY " +
//            "AND o.oneDayLongitude <= :maxX " +
//            "AND o.oneDayLongitude >= :minX")
//    List<OneDay> findAroundOneDayList(@Param("maxY") double maxY, @Param("maxX") double maxX,
//                                      @Param("minY") double minY, @Param("minX") double minX);

}
