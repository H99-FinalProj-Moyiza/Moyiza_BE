package com.example.moyiza_be.review.service;


import com.example.moyiza_be.blackList.service.BlackListService;
import com.example.moyiza_be.common.enums.ReviewTypeEnum;
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
import com.example.moyiza_be.review.repository.QueryDSL.ReviewRepositoryCustom;
import com.example.moyiza_be.review.repository.ReviewImageRepository;
import com.example.moyiza_be.review.repository.ReviewRepository;
import com.example.moyiza_be.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final LikeService likeService;
    private final EventService eventService;
    private final OneDayService oneDayService;
    private final BlackListService blackListService;
    private final AwsS3Uploader s3Uploader;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewRepositoryCustom reviewRepositoryCustom;

    @Transactional
    public ResponseEntity<Page<ReviewListResponse>> getReviewList(
            User user, ReviewTypeEnum reviewTypeEnum, Long identifier, Pageable pageable
    )
    {
        List<Long> filteringIdList = blackListService.filtering(user);
        return ResponseEntity.ok(reviewRepositoryCustom.getReviewList(user, reviewTypeEnum, identifier, pageable, filteringIdList));
    }


    @Transactional
    public ResponseEntity<ReviewDetailResponse> getReviewDetail(User user, Long reviewId) {
        ReviewDetailResponse response = reviewRepositoryCustom.getReviewDetails(user, reviewId);
        List<String> imageUrlList = reviewImageRepository.findAllByReviewId(reviewId)
                .stream()
                .map(ReviewImage::getImageUrl)
                .toList();
        response.setImageList(imageUrlList);
        return ResponseEntity.ok(response);
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
        log.info("review Text saved for review : "+ newReview.getTitle());
        //image upload bit
        //needs uploadable checking logic to ensure that review contents and image are Transactional
        List<String> imageUrlList = new ArrayList<>();
        if (imageFileList != null) {
            imageUrlList = s3Uploader.uploadMultipleImg(imageFileList);
            List<ReviewImage> imageEntityList = imageUrlList
                    .stream()
                    .map(i -> new ReviewImage(newReview.getId(), i)).toList();
            reviewImageRepository.saveAll(imageEntityList);
            log.info("review image saved for review : "+ newReview.getTitle());
        }

        return ResponseEntity.ok(new ReviewDetailResponse(newReview, user, imageUrlList));
    }


    @Transactional
    public ResponseEntity<Message> deleteReview(User user, Long reviewId){
        Review review = loadReviewById(reviewId);
        if(!isOwner(user, review)){
            throw new IllegalArgumentException("Not Authorized");
        }
        reviewRepository.deleteById(reviewId);
        log.info("deleting review : "+ review.getTitle());
        return ResponseEntity.ok(new Message("deleted " + review.getTitle()));
    }

    @Transactional
    public ResponseEntity<Message> likeReview(User user, Long reviewId) {
        Review review = loadReviewById(reviewId);
        ResponseEntity<Message> likeServiceResponse = likeService.reviewLike(user.getId(), reviewId);
        if (!likeServiceResponse.getStatusCode().is2xxSuccessful()){
            log.info("Error from Likeservice");
            throw new InternalError("LikeService Error");
        }
        review.addLike();
        return likeServiceResponse;
    }
    @Transactional
    public ResponseEntity<Message> cancelLikeReview(User user, Long reviewId) {
        Review review = loadReviewById(reviewId);
        ResponseEntity<Message> likeServiceResponse = likeService.cancelReviewLike(user.getId(), reviewId);
        if (!likeServiceResponse.getStatusCode().is2xxSuccessful()){
            log.info("Error from Likeservice");
            throw new InternalError("LikeService Error");
        }
        review.minusLike();
        return likeServiceResponse;

    }





    ////////////////////////////////


    private Review loadReviewById(Long reviewId){
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NullPointerException("Review Not Found"));
    }

    private Boolean isOwner(User user, Review review){
        return user.getId().equals(review.getWriterId());
    }
}
