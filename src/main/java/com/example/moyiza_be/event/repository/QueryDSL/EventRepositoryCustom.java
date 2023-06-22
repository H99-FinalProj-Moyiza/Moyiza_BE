package com.example.moyiza_be.event.repository.QueryDSL;

import com.example.moyiza_be.event.dto.EventSimpleDetailDto;
import com.example.moyiza_be.event.dto.QEventSimpleDetailDto;
import com.example.moyiza_be.user.entity.QUser;
import com.example.moyiza_be.user.entity.User;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.moyiza_be.event.entity.QEvent.event;
import static com.example.moyiza_be.event.entity.QEventAttendant.eventAttendant;
import static com.example.moyiza_be.like.entity.QEventLike.eventLike;
import static com.example.moyiza_be.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class EventRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public List<EventSimpleDetailDto> getClubEventList(Long clubId, User nowUser, List<Long> filteringIdList){
        Long userId = nowUser == null ? -1 : nowUser.getId();
        QUser owner = new QUser("owner");

        SubQueryExpression<Long> subQuery = JPAExpressions.select(eventAttendant.eventId)
                .from(eventAttendant)
                .where(eventAttendant.userId.in(filteringIdList));

        return jpaQueryFactory
                .from(event)
                .join(eventAttendant).on(eventAttendant.eventId.eq(event.id))
                .join(user).on(eventAttendant.userId.eq(user.id))
                .join(owner).on(event.ownerId.eq(owner.id))
                .where(
                        event.deleted.isFalse(),
                        filteringBlackList(filteringIdList),
                        eventAttendant.eventId.notIn(subQuery),
                        event.clubId.eq(clubId)
                )
                .select(
                        new QEventSimpleDetailDto(
                                event.id,
                                owner.nickname,
                                event.clubId,
                                event.eventTitle,
                                event.eventContent,
                                event.eventLocation,
                                event.eventLatitude,
                                event.eventLongitude,
                                event.eventStartTime,
                                event.eventGroupSize,
                                event.attendantsNum,
                                event.image,
                                event.numLikes,
                                JPAExpressions
                                        .selectFrom(eventLike)
                                        .where(eventLike.eventId.eq(event.id)
                                                .and(eventLike.userId.eq(userId))
                                        )
                                        .exists()
                        )
                )
                .fetch();
    }

    private BooleanExpression filteringBlackList(List<Long> filteringIdList) {
        return filteringIdList.isEmpty() ? null : eventAttendant.userId.notIn(filteringIdList);
    }
}
