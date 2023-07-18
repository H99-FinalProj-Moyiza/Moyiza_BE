package com.example.moyiza_be.domain.oneday.repository;

import com.example.moyiza_be.domain.oneday.entity.OneDayCreate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OneDayCreateRepository extends JpaRepository<OneDayCreate, Long> {
    Optional<OneDayCreate> findByOwnerIdAndConfirmedIsFalse(Long ownerId);

    Optional<OneDayCreate> findByIdAndOwnerIdAndConfirmedIsFalse(Long createOneDayId, Long ownerId);
}
