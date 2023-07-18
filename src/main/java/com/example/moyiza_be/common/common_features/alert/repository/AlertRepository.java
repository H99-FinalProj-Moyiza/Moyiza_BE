package com.example.moyiza_be.common.common_features.alert.repository;

import com.example.moyiza_be.common.common_features.alert.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findAllByReceiver(String userName);
    List<Alert> findAllBySender(String userName);
}