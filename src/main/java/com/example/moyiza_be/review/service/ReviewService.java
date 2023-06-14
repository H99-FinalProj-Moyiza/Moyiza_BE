package com.example.moyiza_be.review.service;


import com.example.moyiza_be.common.security.userDetails.UserDetailsImpl;
import com.example.moyiza_be.common.utils.Message;
import com.example.moyiza_be.like.service.LikeService;
import com.example.moyiza_be.review.dto.ReviewDetailResponse;
import com.example.moyiza_be.review.dto.ReviewListResponse;
import com.example.moyiza_be.review.dto.ReviewPostRequest;
import com.example.moyiza_be.review.repository.ReviewRepository;
import com.example.moyiza_be.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final LikeService likeService;

    @Transactional
    public ResponseEntity<ReviewListResponse> getReviewList(User user) {
        return null;
    }


    @Transactional
    public ResponseEntity<ReviewDetailResponse> getReviewDetail(User user, Long reviewId) {
        return null;
    }

    @Transactional
    public ResponseEntity<ReviewDetailResponse> postReview(ReviewPostRequest reviewPostRequest, List<MultipartFile> imageList, UserDetailsImpl userDetails) {
        return null;
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
