package com.example.moyiza_be.club.repository.QueryDSL;

import com.example.moyiza_be.club.dto.ClubDetailResponse;
import com.example.moyiza_be.club.dto.ClubListResponse;
import com.example.moyiza_be.club.dto.QClubDetailResponse;
import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.user.entity.QUser;
import com.example.moyiza_be.user.entity.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.moyiza_be.club.entity.QClub.club;
import static com.example.moyiza_be.club.entity.QClubImageUrl.clubImageUrl;
import static com.example.moyiza_be.club.entity.QClubJoinEntry.clubJoinEntry;
import static com.example.moyiza_be.like.entity.QClubLike.clubLike;
import static com.example.moyiza_be.user.entity.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.ExpressionUtils.count;


@Repository
@RequiredArgsConstructor
public class ClubRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<ClubListResponse> filteredClubResponseList(
            Pageable pageable, CategoryEnum categoryEnum, String q, String tag1, String tag2, String tag3, User nowUser,
            Long profileId, List<Long> blackClubIdList
    ) {
        Long userId = nowUser == null ? -1 : nowUser.getId();

        List<ClubListResponse> clubListResponseList =
                jpaQueryFactory
                        .from(club)
                        .join(user).on(club.ownerId.eq(user.id))
                        .leftJoin(clubImageUrl).on(clubImageUrl.clubId.eq(club.id))
                        .where(
                                club.isDeleted.isFalse(),
                                filteringBlackList(blackClubIdList),
                                eqCategory(categoryEnum),
                                titleContainOrContentContain(q),
                                eqTag1(tag1),
                                eqTag2(tag2),
                                eqTag3(tag3),
                                isProfileId(profileId)
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(club.id.desc())
                        .transform(
                                groupBy(club.id)
                                        .list(Projections.constructor(ClubListResponse.class,
                                                        club.id,
                                                        user.nickname,
                                                        club.title,
                                                        club.content,
                                                        club.tagString,
                                                        club.maxGroupSize,
                                                        club.nowMemberCount,
                                                        club.thumbnailUrl,
                                                        GroupBy.list(clubImageUrl.imageUrl),
                                                        club.numLikes,
                                                        JPAExpressions
                                                                .selectFrom(clubLike)
                                                                .where(clubLike.clubId.eq(club.id)
                                                                        .and(clubLike.userId.eq(userId))
                                                                )
                                                                .exists()
                                                ))
                        );


//        Long count = jpaQueryFactory
//                .select(club.count())
//                .fetchOne();

        return new PageImpl<>(clubListResponseList, pageable, 5000L);
    }

    public Page<ClubListResponse> filteredJoinedClubResponseList(
            Pageable pageable, User nowUser, Long profileId, List<Long> blackClubIdList
    ) {
        QUser owner = new QUser("owner");

        List<ClubListResponse> clubListResponseList =
                jpaQueryFactory
                        .from(club)
                        .join(clubJoinEntry).on(clubJoinEntry.clubId.eq(club.id))
                        .join(user).on(clubJoinEntry.userId.eq(user.id))
                        .join(owner).on(club.ownerId.eq(owner.id))
                        .leftJoin(clubImageUrl).on(clubImageUrl.clubId.eq(club.id))
                        .where(
                                club.isDeleted.isFalse(),
                                filteringBlackList(blackClubIdList),
                                user.id.eq(profileId)
                        )
                        .distinct()
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(club.id.desc())
                        .transform(
                                groupBy(club.id)
                                        .list(Projections.constructor(ClubListResponse.class,
                                                club.id,
                                                owner.nickname,
                                                club.title,
                                                club.content,
                                                club.tagString,
                                                club.maxGroupSize,
                                                club.nowMemberCount,
                                                club.thumbnailUrl,
                                                GroupBy.list(clubImageUrl.imageUrl),
                                                club.numLikes,
                                                JPAExpressions
                                                        .selectFrom(clubLike)
                                                        .where(clubLike.clubId.eq(club.id)
                                                                .and(clubLike.userId.eq(nowUser.getId()))
                                                        )
                                                        .exists()
                                        ))
                        );
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
                                        .exists(),
                                club.clubRule
                        )
                )
                .from(club)
                .join(user).on(club.ownerId.eq(user.id))
                .where(club.id.eq(clubId))
                .fetchOne();
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

    public List<Club> findMostLikedClubsFilteredBlackList(List<Long> blackClubIdList) {
        return jpaQueryFactory
                .selectFrom(club)
                .where(
                        club.isDeleted.isFalse(),
                        filteringBlackList(blackClubIdList))
                .orderBy(club.numLikes.desc())
                .fetch();
    }

    public Page<ClubListResponse> likeClubResponseList(
            Pageable pageable, Long profileId, List<Long> blackClubIdList
    ) {
        QUser owner = new QUser("owner");

        List<ClubListResponse> clubListResponseList =
                jpaQueryFactory
                        .from(club)
                        .join(clubLike).on(clubLike.clubId.eq(club.id))
                        .join(user).on(clubLike.userId.eq(user.id))
                        .join(owner).on(club.ownerId.eq(owner.id))
                        .leftJoin(clubImageUrl).on(clubImageUrl.clubId.eq(club.id))
                        .where(
                                club.isDeleted.isFalse(),
                                filteringBlackList(blackClubIdList),
                                user.id.eq(profileId)
                        )
                        .distinct()
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(club.id.desc())
                        .transform(
                                groupBy(club.id)
                                        .list(Projections.constructor(ClubListResponse.class,
                                                club.id,
                                                owner.nickname,
                                                club.title,
                                                club.content,
                                                club.tagString,
                                                club.maxGroupSize,
                                                club.nowMemberCount,
                                                club.thumbnailUrl,
                                                GroupBy.list(clubImageUrl.imageUrl),
                                                club.numLikes
                                        ))
                        );
        return new PageImpl<>(clubListResponseList, pageable, 5000L);
    }

    private BooleanExpression isDeletedFalse(){
        return club.isDeleted.eq(false);
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

    private BooleanExpression isProfileId(Long profileId) {
        return profileId == null ? null : club.ownerId.eq(profileId);
    }

    private BooleanExpression filteringBlackList(List<Long> blackClubIdList) {
        return blackClubIdList.isEmpty() ? null : club.id.notIn(blackClubIdList);
    }

}
