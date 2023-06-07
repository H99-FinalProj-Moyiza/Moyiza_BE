package com.example.moyiza_be.oneday.repository;

import com.example.moyiza_be.oneday.entity.OneDayImageUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface OneDayImageUrlRepository extends JpaRepository<OneDayImageUrl, Long> {
    static List<OneDayImageUrl> findAllByOneDayId(Long oneDayId) {
        return null;
    }

    List<OneDayImageUrl> findAllByOneDayCreateId(Long oneDayCreateId);
}
