package com.example.moyiza_be.blackList.repository.QueryDSL;

import com.example.moyiza_be.blackList.dto.BlackListMemberResponse;

import com.example.moyiza_be.blackList.dto.QBlackListMemberResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.moyiza_be.blackList.entity.QBlackList.blackList;
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

}
