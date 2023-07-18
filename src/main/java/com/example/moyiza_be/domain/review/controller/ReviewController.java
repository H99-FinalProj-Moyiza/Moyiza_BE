package com.example.moyiza_be.domain.review.controller;

import com.example.moyiza_be.common.enums.ReviewTypeEnum;
import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.domain.review.dto.ReviewListResponse;
import com.example.moyiza_be.domain.review.dto.ReviewDetailResponse;
import com.example.moyiza_be.domain.review.dto.ReviewPostRequest;
import com.example.moyiza_be.domain.review.service.ReviewService;
import com.example.moyiza_be.domain.user.entity.User;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<Page<ReviewListResponse>> getReviewList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam @Nullable String reviewType,
            @RequestParam @Nullable Long identifier,
            @PageableDefault(page = 0, size = 8, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ){
        ReviewTypeEnum reviewTypeEnum = reviewType == null ? null : ReviewTypeEnum.valueOf(reviewType);
        User user = userDetails == null ? null : userDetails.getUser();
        return reviewService.getReviewList(user, reviewTypeEnum, identifier, pageable);
    }

    @GetMapping("/{review_id}")
    public ResponseEntity<ReviewDetailResponse> getReviewDetail(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long review_id
    ){
        User user = userDetails == null ? null : userDetails.getUser();
        return reviewService.getReviewDetail(user, review_id);
    }

        @PostMapping
        public ResponseEntity<ReviewDetailResponse> postReview(
                @RequestPart @Nullable List<MultipartFile> image,
                @RequestPart ReviewPostRequest reviewPostRequest,
                @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
            User user = userDetails.getUser();
            return reviewService.postReview(reviewPostRequest, image, user);
    }

    @DeleteMapping("/{review_id}")
    public ResponseEntity<Message> deleteReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long review_id
    ){
        User user = userDetails.getUser();
        return reviewService.deleteReview(user, review_id);
    }

    @PostMapping("/{review_id}/like")
    public ResponseEntity<Message> likeReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long review_id
    ){
        User user = userDetails.getUser();
        return reviewService.likeReview(user, review_id);
    }

    @DeleteMapping("/{review_id}/like")
    public ResponseEntity<Message> cancelLikeReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long review_id
    ){
        User user = userDetails.getUser();
        return reviewService.cancelLikeReview(user, review_id);
    }
}
