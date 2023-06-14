package com.example.moyiza_be.like.repository;

import com.example.moyiza_be.like.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    Boolean existsByUserIdAndReviewId(Long userId, Long reviewId);
    void deleteByUserIdAndReviewId(Long userId, Long reviewId);

}
