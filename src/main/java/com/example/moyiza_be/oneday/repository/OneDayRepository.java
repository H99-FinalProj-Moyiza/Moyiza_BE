package com.example.moyiza_be.oneday.repository;

import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.oneday.entity.OneDay;
import com.example.moyiza_be.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
