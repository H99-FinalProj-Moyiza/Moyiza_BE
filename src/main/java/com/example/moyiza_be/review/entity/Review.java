package com.example.moyiza_be.review.entity;


import com.example.moyiza_be.common.enums.ReviewTypeEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long writerId;
    private ReviewTypeEnum reviewType;
    private Long identifier;
    private String title;
    private String textContent;

    public Review(
            Long writerId, ReviewTypeEnum reviewType, Long identifier,
            String title, String textContent
    ) {
        this.writerId = writerId;
        this.reviewType = reviewType;
        this.identifier = identifier;
        this.title = title;
        this.textContent = textContent;
    }
}
