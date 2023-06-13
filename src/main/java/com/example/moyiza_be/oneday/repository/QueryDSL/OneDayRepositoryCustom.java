package com.example.moyiza_be.oneday.repository.QueryDSL;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.oneday.dto.OneDayListResponseDto;
import com.example.moyiza_be.oneday.dto.QOneDayListResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.moyiza_be.oneday.entity.QOneDay.oneDay;
import static com.example.moyiza_be.user.entity.QUser.user;
import static com.querydsl.core.types.dsl.MathExpressions.power;


@Repository
@RequiredArgsConstructor
public class OneDayRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<OneDayListResponseDto> getFilteredOnedayList(
            Pageable pageable, CategoryEnum category, String q, String tag1, String tag2, String tag3,
            Double nowLongitude, Double nowLatitude, Double radius, LocalDateTime timeCondition
    ){
        List<OneDayListResponseDto> onedayList =
                jpaQueryFactory
                .select(
                        new QOneDayListResponseDto(
                        oneDay.id,
                        user.nickname,
                        oneDay.oneDayTitle,
                        oneDay.tagString,
                        oneDay.oneDayGroupSize,
                        oneDay.attendantsNum,
                        oneDay.oneDayImage,
                        oneDay.oneDayLongitude,
                        oneDay.oneDayLatitude,
                        oneDay.oneDayLocation
                        )
                )
                .from(oneDay)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .join(user).on(oneDay.ownerId.eq(user.id))
                .where(
                        oneDay.deleted.isFalse(),
                        eqTag1(tag1),
                        eqTag2(tag2),
                        eqTag3(tag3),
                        eqCategory(category),
                        titleContainOrContentContain(q),
                        nearby(radius, nowLongitude, nowLatitude),
                        startTimeAfter(timeCondition)
                )
                .fetch();

        return new PageImpl<>(onedayList,pageable, 1000L);
    }

    private BooleanExpression titleContainOrContentContain(String q) {
        return q == null ? null : titleContain(q).or(contentContain(q));
    }

    private BooleanExpression startTimeAfter(LocalDateTime timeCondition){
        return timeCondition == null ? null : oneDay.oneDayStartTime.after(timeCondition);
    }

    private BooleanExpression eqTag1(String tag) {
        if (tag == null) { return null; }
        int tagIndex = TagEnum.fromString(tag).ordinal();
        return oneDay.tagString.charAt(tagIndex).eq('1');
    }

    private BooleanExpression eqTag2(String tag) {
        if (tag == null) { return null; }
        int tagIndex = TagEnum.fromString(tag).ordinal();
        return oneDay.tagString.charAt(tagIndex).eq('1');
    }

    private BooleanExpression eqTag3(String tag) {
        if (tag == null) { return null; }
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

    private BooleanExpression nearby(Double radius, Double nowLongitude, Double nowLatitude){
        if(radius == null || nowLongitude == null || nowLatitude == null){
            return null;
        } else{
            NumberExpression<Double> latD = power(oneDay.oneDayLatitude.subtract(nowLatitude), 2);
            NumberExpression<Double> longD = power(oneDay.oneDayLongitude.subtract(nowLongitude),2);
            return latD.add(longD).multiply(6371).loe(Math.pow(radius,2));
        }
    }








}
