package com.example.moyiza_be.oneday.repository;

import com.example.moyiza_be.oneday.entity.OneDay;
import com.example.moyiza_be.user.entity.User;
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

    //경도 위도 순
    @Query(value = "SELECT o, ST_Distance_Sphere(:location, POINT(o.oneDayLongitude, o.oneDayLatitude)) AS distance " +
            "FROM OneDay o " +
            "WHERE ST_Distance_Sphere(:location, POINT(o.oneDayLongitude, o.oneDayLatitude)) <= 1000 " +
            "ORDER BY distance")
    List<OneDay> findAroundOneDayList(@Param("location") String location);

//    @Query(value = "SELECT o FROM OneDay o " +
//            "WHERE o.oneDayLatitude <= :maxY " +
//            "AND o.oneDayLatitude >= :minY " +
//            "AND o.oneDayLongitude <= :maxX " +
//            "AND o.oneDayLongitude >= :minX")
//    List<OneDay> findAroundOneDayList(@Param("maxY") double maxY, @Param("maxX") double maxX,
//                                      @Param("minY") double minY, @Param("minX") double minX);
}
