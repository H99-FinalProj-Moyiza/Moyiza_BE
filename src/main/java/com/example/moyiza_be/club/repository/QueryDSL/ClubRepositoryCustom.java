package com.example.moyiza_be.club.repository.QueryDSL;

import com.example.moyiza_be.club.dto.ClubListResponse;
import com.example.moyiza_be.club.dto.QClubListResponse;
import com.example.moyiza_be.club.dto.QClubMemberResponse;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.moyiza_be.club.entity.QClub.club;
import static com.example.moyiza_be.user.entity.QUser.user;
import static com.example.moyiza_be.club.entity.QClubJoinEntry.clubJoinEntry;
import static com.example.moyiza_be.club.entity.QClubImageUrl.clubImageUrl;
import static com.querydsl.core.types.ExpressionUtils.count;
import static com.querydsl.jpa.JPAExpressions.select;

@Repository
@RequiredArgsConstructor
public class ClubRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<ClubListResponse> filteredClubResponseList(
            Pageable pageable, CategoryEnum categoryEnum, String q, String tag1, String tag2, String tag3
    ) {
        List<ClubListResponse> clubListResponseList =
                jpaQueryFactory
                        .select(
                                new QClubListResponse(
                                        club.id,
                                        user.nickname,
                                        club.title,
                                        club.tagString,
                                        club.maxGroupSize,
                                        club.nowMemberCount,
                                        club.thumbnailUrl
                                )
                        )

                        .from(club)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .join(user).on(club.ownerId.eq(user.id))
                        .where(
                                club.isDeleted.eq(Boolean.FALSE),
                                eqCategory(categoryEnum),
                                titleContainOrContentContain(q),
                                eqTag1(tag1),
                                eqTag2(tag2),
                                eqTag3(tag3)
                        )
                        .fetch();
//        Long count = jpaQueryFactory
//                .select(club.count())
//                .fetchOne();

        return new PageImpl<>(clubListResponseList, pageable, 5000L);
    }

    private BooleanExpression titleContainOrContentContain(String q) {
        return q == null ? null : titleContain(q).or(contentContain(q));
    }

    private BooleanExpression eqTag1(String tag) {
        if (tag == null) { return null; }
        int tagIndex = TagEnum.fromString(tag).ordinal();
        return club.tagString.charAt(tagIndex).eq('1');
    }

    private BooleanExpression eqTag2(String tag) {
        if (tag == null) { return null; }
        int tagIndex = TagEnum.fromString(tag).ordinal();
        return club.tagString.charAt(tagIndex).eq('1');
    }

    private BooleanExpression eqTag3(String tag) {
        if (tag == null) { return null; }
        int tagIndex = TagEnum.fromString(tag).ordinal();
        return club.tagString.charAt(tagIndex).eq('1');
    }

    private BooleanExpression eqCategory(CategoryEnum categoryEnum) {
        return categoryEnum == null ? null : club.category.eq(categoryEnum);
    }

    private BooleanExpression titleContain(String q) {
        return q == null ? null : club.title.contains(q);
    }

    private BooleanExpression contentContain(String q) {
        return q == null ? null : club.content.contains(q);
    }


}
