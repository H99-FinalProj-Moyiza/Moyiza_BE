package com.example.moyiza_be.domain.review.dto;

import com.example.moyiza_be.common.enums.ReviewTypeEnum;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ReviewListResponse {
    private final Long reviewId;
    private final String reviewType;
    private final Long identifier;
    private final Long writerId;
    private final String writerNickname;
    private final String writerProfileUrl;
    private final String title;
    private final List<String> imageList;
    private final Integer numLikes;
    private final Boolean isLikedByUser;
    private final LocalDateTime createdAt;
    @QueryProjection
    public ReviewListResponse(Long reviewId,ReviewTypeEnum reviewTypeEnum, Long identifier, Long writerId,
                              String writerNickname,String writerProfileUrl, String title, List<String> imageUrlList, Integer numLikes, Boolean isLikedByUser,
                              LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.reviewType = reviewTypeEnum.toString();
        this.identifier = identifier;
        this.writerId = writerId;
        this.writerNickname = writerNickname;
        this.writerProfileUrl = writerProfileUrl;
        this.title = title;
        this.imageList = imageUrlList;
        this.numLikes = numLikes;
        this.isLikedByUser = isLikedByUser;
        this.createdAt = createdAt;
    }
}
