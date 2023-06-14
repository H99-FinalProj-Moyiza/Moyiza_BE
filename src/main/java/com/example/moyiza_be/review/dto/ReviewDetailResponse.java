package com.example.moyiza_be.review.dto;

import com.example.moyiza_be.review.entity.Review;
import com.example.moyiza_be.review.entity.ReviewImage;
import com.example.moyiza_be.user.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


@Getter
public class ReviewDetailResponse {
    private final Long reviewId;
    private final Long writerId;
    private final String writerNickname;
    private final String title;
    private final String content;
    private final List<String> imageUrlList;

    public ReviewDetailResponse(Review review, User user, List<String> imageUrlList) {
        this.reviewId = review.getId();
        this.writerId = review.getWriterId();
        this.writerNickname = user.getNickname();
        this.title = review.getTitle();
        this.content = review.getTextContent();
        this.imageUrlList = imageUrlList;
    }

    @QueryProjection
    public ReviewDetailResponse(Long reviewId, Long writerId, String writerNickname,
                                String title, String content, List<ReviewImage> imageEntityList) {
        this.reviewId = reviewId;
        this.writerId = writerId;
        this.writerNickname = writerNickname;
        this.title = title;
        this.content = content;
        this.imageUrlList = imageEntityList.stream().map(ReviewImage::getImageUrl).toList();
    }
}
