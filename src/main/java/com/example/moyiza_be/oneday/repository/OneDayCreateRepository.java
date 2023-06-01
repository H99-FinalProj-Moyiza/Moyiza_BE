package com.example.moyiza_be.oneday.repository;

import com.example.moyiza_be.oneday.entity.OneDayCreate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OneDayCreateRepository extends JpaRepository<OneDayCreate, Long> {
    Optional<OneDayCreate> findByOwnerIdAndConfirmedIsFalse(Long ownerId);

    Optional<OneDayCreate> findByIdAndConfirmedIsFalse(Long createOneDayId);
}
