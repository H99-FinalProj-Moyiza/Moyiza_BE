package com.example.moyiza_be.review.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ReviewImage {
    @Id
    @GeneratedValue
    public Long id;
    public Long reviewId;
    public String imageUrl;

    public ReviewImage(Long reviewId, String imageUrl) {
        this.reviewId = reviewId;
        this.imageUrl = imageUrl;
    }
}
