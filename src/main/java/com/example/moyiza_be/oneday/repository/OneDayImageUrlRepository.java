package com.example.moyiza_be.oneday.repository;

import com.example.moyiza_be.oneday.entity.OneDayImageUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OneDayImageUrlRepository extends JpaRepository<OneDayImageUrl, Long> {
    List<OneDayImageUrl> findAllByOneDayId(Long oneDayId);

    List<OneDayImageUrl> findAllByOneDayCreateId(Long oneDayCreateId);

    Optional<OneDayImageUrl> findFirstByOneDayId(Long onedayId);

    List<OneDayImageUrl> findAllByOneDayIdIn(List<Long> oneDaysInOperationIds);

    void deleteAllByOneDayCreateId(Long createOneDayId);
}
