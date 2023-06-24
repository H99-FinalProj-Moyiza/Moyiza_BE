package com.example.moyiza_be.club.repository;

import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.common.enums.CategoryEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Page<Club> findByCategoryAndIsDeletedFalseAndTitleContaining(Pageable pageable, CategoryEnum category, String q);

    Page<Club> findByCategoryAndIsDeletedFalse(Pageable pageable, CategoryEnum category);

    Page<Club> findByIsDeletedFalseAndTitleContaining(Pageable pageable, String q);

    Integer countByOwnerIdAndIsDeletedFalse(Long userId);


    Boolean existsByIdAndIsDeletedFalseAndOwnerIdEquals(Long clubId, Long userId);
    Page<Club> findAllByIsDeletedFalse(Pageable pageable);
    Optional<Club> findByIdAndIsDeletedFalse(Long clubId);

    List<Club> findByOwnerId(Long userId);
    List<Club> findAllByIsDeletedFalseOrderByNumLikesDesc();

    @Transactional
    @Modifying
    @Query("DELETE FROM Club c WHERE c.isDeleted=true AND c.modifiedAt < :targetDate")
    void cleanUpDeletedClubs(@Param("targetDate") LocalDateTime targetDate);

}
