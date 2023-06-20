package com.example.moyiza_be.oneday.repository.QueryDSL;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.oneday.dto.OneDayListResponseDto;
import com.example.moyiza_be.user.entity.QUser;
import com.example.moyiza_be.user.entity.User;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.moyiza_be.like.entity.QOnedayLike.onedayLike;
import static com.example.moyiza_be.oneday.entity.QOneDay.oneDay;
import static com.example.moyiza_be.oneday.entity.QOneDayAttendant.oneDayAttendant;
import static com.example.moyiza_be.oneday.entity.QOneDayImageUrl.oneDayImageUrl;
import static com.example.moyiza_be.user.entity.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.dsl.MathExpressions.power;


@Repository
@RequiredArgsConstructor
public class OneDayRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<OneDayListResponseDto> getFilteredOnedayList(
            User nowUser, Long profileId, Pageable pageable, CategoryEnum category, String q, String tag1, String tag2, String tag3,
            Double nowLongitude, Double nowLatitude, Double radius, LocalDateTime timeCondition
    ) {
        Long userId = nowUser == null ? -1 : nowUser.getId();
        List<OneDayListResponseDto> onedayList =
                jpaQueryFactory
                        .from(oneDay)
                        .join(user).on(oneDay.ownerId.eq(user.id))
                        .leftJoin(oneDayImageUrl).on(oneDay.id.eq(oneDayImageUrl.oneDayId))
                        .where(
                                oneDay.deleted.isFalse(),
                                eqTag1(tag1),
                                eqTag2(tag2),
                                eqTag3(tag3),
                                eqCategory(category),
                                titleContainOrContentContain(q),
                                nearby(radius, nowLongitude, nowLatitude),
                                startTimeAfter(timeCondition),
                                isProfileId(profileId)
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(oneDay.id.desc())
                        .transform(
                                groupBy(oneDay.id)
                                        .list(
                                                Projections.constructor(OneDayListResponseDto.class,
                                                        oneDay.id,
                                                        user.nickname,
                                                        user.profileImage,
                                                        oneDay.oneDayTitle,
                                                        oneDay.oneDayContent,
                                                        oneDay.tagString,
                                                        oneDay.oneDayGroupSize,
                                                        oneDay.attendantsNum,
                                                        oneDay.oneDayImage,
                                                        GroupBy.list(oneDayImageUrl.imageUrl),
                                                        oneDay.oneDayLongitude,
                                                        oneDay.oneDayLatitude,
                                                        oneDay.oneDayLocation,
                                                        oneDay.numLikes,
                                                        JPAExpressions
                                                                .selectFrom(onedayLike)
                                                                .where(onedayLike.onedayId.eq(oneDay.id)
                                                                        .and(onedayLike.userId.eq(userId))
                                                                )
                                                                .exists()

                                                )
                                        )

                        );

        return new PageImpl<>(onedayList, pageable, 1000L);
    }

    public Page<OneDayListResponseDto> getFilteredJoinedOnedayList(
            User nowUser, Long profileId, Pageable pageable
    ) {
        QUser owner = new QUser("owner");
        List<OneDayListResponseDto> onedayList =
                jpaQueryFactory
                        .from(oneDay)
                        .join(oneDayAttendant).on(oneDayAttendant.oneDayId.eq(oneDay.id))
                        .join(user).on(oneDayAttendant.userId.eq(user.id))
                        .join(owner).on(oneDay.ownerId.eq(owner.id))
                        .leftJoin(oneDayImageUrl).on(oneDay.id.eq(oneDayImageUrl.oneDayId))
                        .where(
                                user.id.eq(profileId),
                                oneDay.deleted.isFalse()
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(oneDay.id.desc())
                        .transform(
                                groupBy(oneDay.id)
                                        .list(
                                                Projections.constructor(OneDayListResponseDto.class,
                                                        oneDay.id,
                                                        owner.nickname,
                                                        user.profileImage,
                                                        oneDay.oneDayTitle,
                                                        oneDay.oneDayContent,
                                                        oneDay.tagString,
                                                        oneDay.oneDayGroupSize,
                                                        oneDay.attendantsNum,
                                                        oneDay.oneDayImage,
                                                        GroupBy.list(oneDayImageUrl.imageUrl),
                                                        oneDay.oneDayLongitude,
                                                        oneDay.oneDayLatitude,
                                                        oneDay.oneDayLocation,
                                                        oneDay.numLikes,
                                                        JPAExpressions
                                                                .selectFrom(onedayLike)
                                                                .where(onedayLike.onedayId.eq(oneDay.id)
                                                                        .and(onedayLike.userId.eq(nowUser.getId()))
                                                                )
                                                                .exists()

                                                )
                                        )

                        );

        return new PageImpl<>(onedayList, pageable, 1000L);
    }

    private BooleanExpression titleContainOrContentContain(String q) {
        return q == null ? null : titleContain(q).or(contentContain(q));
    }

    private BooleanExpression startTimeAfter(LocalDateTime timeCondition) {
        return timeCondition == null ? null : oneDay.oneDayStartTime.after(timeCondition);
    }

    private BooleanExpression eqTag1(String tag) {
        if (tag == null) {
            return null;
        }
        int tagIndex = TagEnum.fromString(tag).ordinal();
        return oneDay.tagString.charAt(tagIndex).eq('1');
    }

    private BooleanExpression eqTag2(String tag) {
        if (tag == null) {
            return null;
        }
        int tagIndex = TagEnum.fromString(tag).ordinal();
        return oneDay.tagString.charAt(tagIndex).eq('1');
    }

    private BooleanExpression eqTag3(String tag) {
        if (tag == null) {
            return null;
        }
        int tagIndex = TagEnum.fromString(tag).ordinal();
        return oneDay.tagString.charAt(tagIndex).eq('1');
    }

    private BooleanExpression eqCategory(CategoryEnum categoryEnum) {
        return categoryEnum == null ? null : oneDay.category.eq(categoryEnum);
    }

    private BooleanExpression titleContain(String q) {
        return q == null ? null : oneDay.oneDayTitle.contains(q);
    }

    private BooleanExpression contentContain(String q) {
        return q == null ? null : oneDay.oneDayContent.contains(q);
    }

    private BooleanExpression nearby(Double radius, Double nowLongitude, Double nowLatitude) {
        if (radius == null || nowLongitude == null || nowLatitude == null) {
            return null;
        } else {
            NumberExpression<Double> latD = power(oneDay.oneDayLatitude.subtract(nowLatitude), 2);
            NumberExpression<Double> longD = power(oneDay.oneDayLongitude.subtract(nowLongitude), 2);
            return latD.add(longD).multiply(6371).loe(Math.pow(radius, 2));
        }
    }

    private BooleanExpression isProfileId(Long profileId) {
        return profileId == null ? null : user.id.eq(profileId);
    }

}
