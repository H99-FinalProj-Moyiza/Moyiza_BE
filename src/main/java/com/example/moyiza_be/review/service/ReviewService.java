package com.example.moyiza_be.review.service;


import com.example.moyiza_be.club.entity.ClubImageUrl;
import com.example.moyiza_be.common.enums.ReviewTypeEnum;
import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import com.example.moyiza_be.common.utils.AwsS3Uploader;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.event.service.EventService;
import com.example.moyiza_be.like.service.LikeService;
import com.example.moyiza_be.oneday.service.OneDayService;
import com.example.moyiza_be.review.dto.ReviewDetailResponse;
import com.example.moyiza_be.review.dto.ReviewListResponse;
import com.example.moyiza_be.review.dto.ReviewPostRequest;
import com.example.moyiza_be.review.entity.Review;
import com.example.moyiza_be.review.entity.ReviewImage;
import com.example.moyiza_be.review.repository.ReviewImageRepository;
import com.example.moyiza_be.review.repository.ReviewRepository;
import com.example.moyiza_be.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final LikeService likeService;
    private final EventService eventService;
    private final OneDayService oneDayService;
    private final AwsS3Uploader s3Uploader;
    private final ReviewImageRepository reviewImageRepository;

    @Transactional
    public ResponseEntity<ReviewListResponse> getReviewList(User user) {
        return null;
    }


    @Transactional
    public ResponseEntity<ReviewDetailResponse> getReviewDetail(User user, Long reviewId) {
        return null;
    }

    @Transactional
    public ResponseEntity<ReviewDetailResponse> postReview(
            ReviewPostRequest reviewPostRequest, List<MultipartFile> imageFileList, User user
    ) {
        ReviewTypeEnum reviewType = reviewPostRequest.getReviewTypeEnum();
        if(ReviewTypeEnum.EVENT.equals(reviewType)){
            eventService.checkValidity(user, reviewPostRequest.getIdentifier());
        }
        if(ReviewTypeEnum.ONEDAY.equals(reviewType)){
            oneDayService.checkValidity(user, reviewPostRequest.getIdentifier());
        }
        Review newReview = reviewPostRequest.toReviewEntity(user.getId());
        reviewRepository.saveAndFlush(newReview);
        //image upload bit
        //needs uploadable checking logic to ensure that review contents and image are Transactional
        List<String> imageUrlList = new ArrayList<>();
        if (imageFileList != null) {
            imageUrlList = s3Uploader.uploadMultipleImg(imageFileList);
            List<ReviewImage> imageEntityList = imageUrlList
                    .stream()
                    .map(i -> new ReviewImage(newReview.getId(), i)).toList();
            reviewImageRepository.saveAll(imageEntityList);
        }

        return ResponseEntity.ok(new ReviewDetailResponse(newReview, user, imageUrlList));
    }


    @Transactional
    public ResponseEntity<Message> deleteReview(User user, Long reviewId) {
        return null;
    }

    @Transactional
    public ResponseEntity<Message> likeReview(User user, Long reviewId) {
        return null;
    }

    @Transactional
    public ResponseEntity<Message> cancelLikeReview(User user, Long reviewId) {
        return null;
    }
}
