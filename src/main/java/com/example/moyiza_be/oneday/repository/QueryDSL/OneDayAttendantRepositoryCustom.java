package com.example.moyiza_be.oneday.repository.QueryDSL;

import com.example.moyiza_be.oneday.dto.OneDayMemberResponse;
import com.example.moyiza_be.oneday.dto.QOneDayMemberResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.moyiza_be.oneday.entity.QOneDayAttendant.oneDayAttendant;
import static com.example.moyiza_be.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class OneDayAttendantRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public List<OneDayMemberResponse> getOneDayMemberList(Long oneDayId) {
        return jpaQueryFactory
                .select(
                        new QOneDayMemberResponse(
                        user.id,
                        user.nickname,
                        user.profileImage
                        )
                )
                .from(oneDayAttendant)
                .leftJoin(user).on(oneDayAttendant.userId.eq(user.id))
                .where(oneDayAttendant.oneDayId.eq(oneDayId))
                .fetch();
    }
}
