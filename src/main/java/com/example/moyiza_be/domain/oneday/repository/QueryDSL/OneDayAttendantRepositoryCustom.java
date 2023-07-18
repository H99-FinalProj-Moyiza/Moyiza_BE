package com.example.moyiza_be.domain.oneday.repository.QueryDSL;

import com.example.moyiza_be.domain.oneday.dto.MemberResponse;
import com.example.moyiza_be.domain.oneday.dto.QMemberResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.moyiza_be.domain.oneday.entity.QOneDayAttendant.oneDayAttendant;
import static com.example.moyiza_be.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class OneDayAttendantRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public List<MemberResponse> getOneDayMemberList(Long oneDayId) {
        return jpaQueryFactory
                .select(
                        new QMemberResponse(
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
