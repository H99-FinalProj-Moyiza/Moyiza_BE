package com.example.moyiza_be.review.dto;


import com.example.moyiza_be.common.enums.ReviewTypeEnum;
import lombok.Getter;

@Getter
public class ReviewPostRequest {
    private final ReviewTypeEnum reviewTypeEnum;
    private final Long identifier;
    private final String title;
    private final String textContent;

    public ReviewPostRequest(String reviewType,Long identifier, String title, String textContent) {
        this.reviewTypeEnum = ReviewTypeEnum.valueOf(reviewType);
        this.identifier = identifier;
        this.title = title;
        this.textContent = textContent;
    }
}
