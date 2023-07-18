package com.example.moyiza_be.domain.oneday.repository;

import com.example.moyiza_be.domain.oneday.entity.OneDayApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OneDayApprovalRepository extends JpaRepository<OneDayApproval, Long> {

    List<OneDayApproval> findAllByOneDayId(Long oneDayId);

    OneDayApproval findByOneDayId(Long oneDayId);

    OneDayApproval findByOneDayIdAndUserId(Long oneDayId, Long userId);

    boolean existsByOneDayIdAndUserId(Long oneDayId, Long id);

    List<OneDayApproval> findAllByOneDayIdAndUserId(Long oneDayId, Long userId);
}
