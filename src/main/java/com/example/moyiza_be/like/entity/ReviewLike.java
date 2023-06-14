package com.example.moyiza_be.like.entity;

import com.example.moyiza_be.common.utils.TimeStamped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class ReviewLike extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long reviewId;

    public ReviewLike(Long userId, Long reviewId) {
        this.userId = userId;
        this.reviewId = reviewId;
    }
}
