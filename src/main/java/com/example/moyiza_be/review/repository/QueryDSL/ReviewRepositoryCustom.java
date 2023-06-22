package com.example.moyiza_be.review.repository.QueryDSL;


import com.example.moyiza_be.common.enums.ReviewTypeEnum;
import com.example.moyiza_be.review.dto.ReviewDetailResponse;
import com.example.moyiza_be.review.dto.ReviewListResponse;
import com.example.moyiza_be.user.entity.User;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.moyiza_be.like.entity.QReviewLike.reviewLike;
import static com.example.moyiza_be.review.entity.QReview.review;
import static com.example.moyiza_be.review.entity.QReviewImage.reviewImage;
import static com.example.moyiza_be.user.entity.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<ReviewListResponse> getReviewList(
            User nowUser, ReviewTypeEnum reviewTypeEnum, Long identifier, Pageable pageable, List<Long> filteringIdList
    ) {
        Long userId = nowUser == null ? -1 : nowUser.getId();
        List<ReviewListResponse> reviewListResponse =
                jpaQueryFactory
                .from(review)
                .join(user).on(review.writerId.eq(user.id))
                .leftJoin(reviewImage).on(reviewImage.reviewId.eq(review.id))
                .where(
                        reviewTypeAndIdentifierMatcher(reviewTypeEnum, identifier),
                        filteringBlackList(filteringIdList)
                )
                .transform(
                        groupBy(review.id)
                                .list(Projections.constructor(ReviewListResponse.class,
                                                review.id,
                                                review.reviewType,
                                                review.identifier,
                                                review.writerId,
                                                user.nickname,
                                                user.profileImage,
                                                review.title,
                                                GroupBy.list(reviewImage.imageUrl),
                                                review.numLikes,
                                                JPAExpressions
                                                        .selectFrom(reviewLike)
                                                        .where(reviewLike.reviewId.eq(review.id)
                                                                .and(reviewLike.userId.eq(userId))
                                                        )
                                                        .exists(),
                                                review.createdAt
                                        )
                                )
                );
        return new PageImpl<>(reviewListResponse, pageable, 1000L);
    }

    public ReviewDetailResponse getReviewDetails(User nowUser, Long reviewId){
        Long userId = nowUser == null ? -1 : nowUser.getId();

        ReviewDetailResponse result = jpaQueryFactory
                .from(review)
                .where(review.id.eq(reviewId))
                .join(user).on(review.writerId.eq(user.id))
//                .leftJoin(reviewImage).on(reviewImage.reviewId.eq(review.id))
                .select(
                        Projections.constructor(ReviewDetailResponse.class,
                                review.id,
                                review.reviewType,
                                review.identifier,
                                review.writerId,
                                user.nickname,
                                user.profileImage,
                                review.title,
                                review.textContent,
//                                GroupBy.list(reviewImage.imageUrl),
                                review.numLikes,
                                JPAExpressions
                                        .selectFrom(reviewLike)
                                        .where(reviewLike.reviewId.eq(review.id)
                                                .and(reviewLike.userId.eq(userId))
                                        )
                                        .exists(),
                                review.createdAt,
                                review.modifiedAt
                        )
                )
                .fetchOne();

        return result;
    }

    BooleanExpression eqReviewType(ReviewTypeEnum reviewTypeEnum){
        return reviewTypeEnum == null ? null : review.reviewType.eq(reviewTypeEnum);
    }

    BooleanExpression eqIdentifier(Long identifier){
        return identifier == null ? null : review.identifier.eq(identifier);
    }
    BooleanExpression reviewTypeAndIdentifierMatcher(ReviewTypeEnum reviewTypeEnum, Long identifier){
        if (reviewTypeEnum == null){
            if(identifier == null){
                return null;
            }
            else{
                throw new NullPointerException("reviewType not specified");
            }
        }
        else{
            if(identifier == null){
                return eqReviewType(reviewTypeEnum);
            }
            else{
                return eqReviewType(reviewTypeEnum).and(eqIdentifier(identifier));
            }
        }
    }

    BooleanExpression filteringBlackList(List<Long> filteringIdList) {
        return filteringIdList.isEmpty() ? null : review.writerId.notIn(filteringIdList);
    }

}
