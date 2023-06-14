package com.example.moyiza_be.like.repository;


import com.example.moyiza_be.like.entity.OnedayLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnedayLikeRepository extends JpaRepository<OnedayLike, Long> {
    Boolean existsByUserIdAndOnedayId(Long userId, Long onedayId);

    void deleteByUserIdAndOnedayId(Long userId, Long onedayId);
}
