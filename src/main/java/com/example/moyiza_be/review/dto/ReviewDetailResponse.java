package com.example.moyiza_be.review.dto;

import com.example.moyiza_be.common.enums.ReviewTypeEnum;
import com.example.moyiza_be.review.entity.Review;
import com.example.moyiza_be.user.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
public class ReviewDetailResponse {
    private final Long reviewId;
    private final String reviewType;
    private final Long identifier;
    private final Long writerId;
    private final String writerNickname;
    private final String writerProfileUrl;
    private final String title;
    private final String textContent;
    private List<String> imageList;
    private final Integer numLikes;
    private final Boolean isLikedByUser;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    @QueryProjection
    public ReviewDetailResponse(Long reviewId, ReviewTypeEnum reviewTypeEnum, Long identifier, Long writerId,
                                String writerNickname, String writerProfileUrl, String title, String content,
//                               List<String> imageUrlList,
                                Integer numLikes, Boolean isLikedByUser,
                                LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.reviewId = reviewId;
        this.reviewType = reviewTypeEnum.toString();
        this.identifier = identifier;
        this.writerId = writerId;
        this.writerNickname = writerNickname;
        this.writerProfileUrl = writerProfileUrl;
        this.title = title;
        this.textContent = content;
        this.imageList = new ArrayList<>();
        this.numLikes = numLikes;
        this.isLikedByUser = isLikedByUser;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public ReviewDetailResponse(Review review, User user, List<String> imageUrlList) {
        this.reviewId = review.getId();
        this.reviewType = review.getReviewType().toString();
        this.identifier = review.getIdentifier();
        this.writerId = review.getWriterId();
        this.writerNickname = user.getNickname();
        this.writerProfileUrl = user.getProfileImage();
        this.title = review.getTitle();
        this.textContent = review.getTextContent();
        this.imageList = imageUrlList;
        this.numLikes = 0;
        this.isLikedByUser = false;
        this.createdAt = review.getCreatedAt();
        this.modifiedAt = review.getModifiedAt();
    }

    public void setImageList(List<String> imageList){
        this.imageList = imageList;
    }


}
