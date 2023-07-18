package com.example.moyiza_be.common.common_features.blackList.repository.QueryDSL;

import com.example.moyiza_be.common.common_features.blackList.dto.BlackListMemberResponse;
import com.example.moyiza_be.blackList.dto.QBlackListMemberResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.example.moyiza_be.blackList.entity.QBlackList.blackList;
import static com.example.moyiza_be.club.entity.QClub.club;
import static com.example.moyiza_be.club.entity.QClubJoinEntry.clubJoinEntry;
import static com.example.moyiza_be.event.entity.QEvent.event;
import static com.example.moyiza_be.event.entity.QEventAttendant.eventAttendant;
import static com.example.moyiza_be.oneday.entity.QOneDay.oneDay;
import static com.example.moyiza_be.oneday.entity.QOneDayAttendant.oneDayAttendant;
import static com.example.moyiza_be.review.entity.QReview.review;
import static com.example.moyiza_be.user.entity.QUser.user;



@Repository
@RequiredArgsConstructor
public class BlackListRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public List<BlackListMemberResponse> getBlackListMembers(Long userId) {
        return jpaQueryFactory
                .select(
                        new QBlackListMemberResponse(
                                user.id,
                                user.nickname,
                                user.email,
                                user.profileImage
                        )
                )
                .from(blackList)
                .join(user).on(blackList.blackListUserId.eq(user.id))
                .where(blackList.userId.eq(userId))
                .fetch();
    }

    public List<Long> getBlackUserIdList(Long userId) {

        List<Long> blackListedIdsByUserId = jpaQueryFactory
                .select(blackList.blackListUserId)
                .from(blackList)
                .where(blackList.userId.eq(userId))
                .fetch();

        List<Long> blackUserIdList = new ArrayList<>(blackListedIdsByUserId);

        List<Long> otherUserIdsBlackListedUserId = jpaQueryFactory
                .select(blackList.userId)
                .from(blackList)
                .where(blackList.blackListUserId.eq(userId))
                .fetch();

        blackUserIdList.addAll(otherUserIdsBlackListedUserId);

        return blackUserIdList.stream().distinct().sorted().toList();
    }

    public List<Long> getBlackClubIdList(List<Long> blackUserIdList, Long userId) {
        return jpaQueryFactory
                .select(
                        club.id
                )
                .from(club)
                .join(clubJoinEntry).on(clubJoinEntry.clubId.eq(club.id))
                .join(user).on(user.id.eq(clubJoinEntry.userId))
                .where(
                        club.isDeleted.isFalse()
                                .and(isBlackClub(blackUserIdList))
                                .and(club.id.notIn(
                                        JPAExpressions.select(clubJoinEntry.clubId)
                                                .from(clubJoinEntry)
                                                .where(clubJoinEntry.userId.eq(userId))
                                ))
                )
                .stream()
                .distinct()
                .sorted()
                .toList();
    }

    public List<Long> getBlackEventIdList(List<Long> blackUserIdList, Long userId) {
        return jpaQueryFactory
                .select(
                        event.id
                )
                .from(event)
                .join(eventAttendant).on(eventAttendant.eventId.eq(event.id))
                .join(user).on(user.id.eq(eventAttendant.userId))
                .where(
                        event.deleted.isFalse()
                                .and(isBlackEvent(blackUserIdList))
                                .and(event.id.notIn(
                                        JPAExpressions.select(eventAttendant.eventId)
                                                .from(eventAttendant)
                                                .where(eventAttendant.userId.eq(userId))
                                ))
                )
                .stream()
                .distinct()
                .sorted()
                .toList();
    }

    public List<Long> getBlackOneDayIdList(List<Long> blackUserIdList, Long userId) {
        return jpaQueryFactory
                .select(
                        oneDay.id
                )
                .from(oneDay)
                .join(oneDayAttendant).on(oneDayAttendant.oneDayId.eq(oneDay.id))
                .join(user).on(user.id.eq(oneDayAttendant.userId))
                .where(
                        oneDay.deleted.isFalse()
                                    .and(isBlackOneDay(blackUserIdList))
                                    .and(oneDay.id.notIn(
                                            JPAExpressions.select(oneDayAttendant.oneDayId)
                                                    .from(oneDayAttendant)
                                                    .where(oneDayAttendant.userId.eq(userId))
                                    ))
                )
                .stream()
                .distinct()
                .sorted()
                .toList();
    }

    public List<Long> getBlackReviewIdList(List<Long> blackUserIdList) {
        return jpaQueryFactory
                .select(
                        review.id
                )
                .from(review)
                .join(user).on(user.id.eq(review.writerId))
                .where(
                        isBlackReview(blackUserIdList)
                )
                .stream()
                .distinct()
                .sorted()
                .toList();
    }

    private BooleanExpression isBlackClub(List<Long> blackUserIdList) {
        return blackUserIdList.isEmpty() ? null : clubJoinEntry.userId.in(blackUserIdList);
    }

    private BooleanExpression isBlackEvent(List<Long> blackUserIdList) {
        return blackUserIdList.isEmpty() ? null : eventAttendant.userId.in(blackUserIdList);
    }

    private BooleanExpression isBlackOneDay(List<Long> blackUserIdList) {
        return blackUserIdList.isEmpty() ? null : oneDayAttendant.userId.in(blackUserIdList);
    }

    private BooleanExpression isBlackReview(List<Long> blackUserIdList) {
        return blackUserIdList.isEmpty() ? null : review.writerId.in(blackUserIdList);
    }
}
