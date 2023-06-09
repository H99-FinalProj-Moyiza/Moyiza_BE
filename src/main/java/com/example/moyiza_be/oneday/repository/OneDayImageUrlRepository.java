package com.example.moyiza_be.oneday.repository;

import com.example.moyiza_be.oneday.entity.OneDayImageUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OneDayImageUrlRepository extends JpaRepository<OneDayImageUrl, Long> {
    List<OneDayImageUrl> findAllByOneDayId(Long oneDayId);

    List<OneDayImageUrl> findAllByOneDayCreateId(Long oneDayCreateId);
}
