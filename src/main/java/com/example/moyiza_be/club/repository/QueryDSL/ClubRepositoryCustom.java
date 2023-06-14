package com.example.moyiza_be.club.repository.QueryDSL;

import com.example.moyiza_be.club.dto.ClubDetailResponse;
import com.example.moyiza_be.club.dto.ClubListResponse;
import com.example.moyiza_be.club.dto.QClubDetailResponse;
import com.example.moyiza_be.club.dto.QClubListResponse;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.like.entity.ClubLike;
import com.example.moyiza_be.like.entity.QClubLike;
import com.example.moyiza_be.user.entity.User;
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
import static com.example.moyiza_be.like.entity.QClubLike.clubLike;


@Repository
@RequiredArgsConstructor
public class ClubRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<ClubListResponse> filteredClubResponseList(
            Pageable pageable, CategoryEnum categoryEnum, String q, String tag1, String tag2, String tag3, User nowUser
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
                                        club.thumbnailUrl,
                                        club.numLikes,
                                        JPAExpressions
                                                .selectFrom(clubLike)
                                                .where(clubLike.clubId.eq(club.id)
                                                        .and(clubLike.userId.eq(nowUser.getId()))
                                                )
                                                .exists()
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
                        .orderBy(club.id.desc())
                        .fetch();
//        Long count = jpaQueryFactory
//                .select(club.count())
//                .fetchOne();

        return new PageImpl<>(clubListResponseList, pageable, 5000L);
    }

    public ClubDetailResponse getClubDetail(Long clubId, User nowUser){
        Long userId = nowUser == null ? -1 : nowUser.getId();
        return jpaQueryFactory
                .select(
                        new QClubDetailResponse(
                                club.id,
                                user.nickname,
                                club.title,
                                club.category,
                                club.tagString,
                                club.content,
                                club.agePolicy,
                                club.genderPolicy,
                                club.maxGroupSize,
                                club.nowMemberCount,
                                club.thumbnailUrl,
                                club.numLikes,
                                JPAExpressions
                                        .selectFrom(clubLike)
                                        .where(clubLike.clubId.eq(club.id)
                                                .and(clubLike.userId.eq(userId))
                                        )
                                        .exists()
                        )
                )
                .from(club)
                .join(user).on(club.ownerId.eq(user.id))
                .where(club.id.eq(clubId))
                .fetchOne();
    }

//운영중인 마이페이지 클럽 리스트 조회
    public List<ClubDetailResponse> getManagedClubDetail(Long userId) {
        return jpaQueryFactory
                .select(
                        new QClubDetailResponse(
                                club.id,
                                user.nickname,
                                club.title,
                                club.category,
                                club.tagString,
                                club.content,
                                club.agePolicy,
                                club.genderPolicy,
                                club.maxGroupSize,
                                club.nowMemberCount,
                                club.thumbnailUrl,
                                club.numLikes,
                                JPAExpressions
                                        .selectFrom(clubLike)
                                        .where(clubLike.clubId.eq(club.id)
                                                .and(clubLike.userId.eq(userId))
                                        )
                                        .exists()
                        )
                )
                .from(club)
                .join(user).on(club.ownerId.eq(userId))
                .where(user.id.eq(userId))
                .fetch();
    }

    public List<ClubDetailResponse> getJoinedClubDetail(Long userId) {
        return jpaQueryFactory
                .select(
                        new QClubDetailResponse(
                                club.id,
                                user.nickname,
                                club.title,
                                club.category,
                                club.tagString,
                                club.content,
                                club.agePolicy,
                                club.genderPolicy,
                                club.maxGroupSize,
                                club.nowMemberCount,
                                club.thumbnailUrl,
                                club.numLikes,
                                JPAExpressions
                                        .selectFrom(clubLike)
                                        .where(clubLike.clubId.eq(club.id)
                                                .and(clubLike.userId.eq(userId))
                                        )
                                        .exists()
                        )
                )
                .from(club)
                .join(clubJoinEntry).on(clubJoinEntry.clubId.eq(club.id))
                .join(user).on(clubJoinEntry.userId.eq(userId))
                .where(user.id.eq(userId))
                .fetch();
    }

//    private OrderSpecifier createOrderSpecifier(OrderCondition orderCondition) {
//
//        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
//
//        if(Objects.isNull(orderCondition)){
//            orderSpecifiers.add(new OrderSpecifier(Order.DESC, person.name));
//        }else if(orderCondition.equals(OrderCondition.AGE)){
//            orderSpecifiers.add(new OrderSpecifier(Order.DESC, person.age));
//        }else{
//            orderSpecifiers.add(new OrderSpecifier(Order.DESC, person.region));
//        }
//        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
//    }

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
