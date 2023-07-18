package com.example.moyiza_be.domain.club.repository.QueryDSL;

import com.example.moyiza_be.domain.club.dto.ClubMemberResponse;
import com.example.moyiza_be.domain.club.dto.QClubMemberResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.moyiza_be.domain.club.entity.QClubJoinEntry.clubJoinEntry;
import static com.example.moyiza_be.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class ClubJoinEntryRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public List<ClubMemberResponse> getClubMemberList(Long clubId){
        return jpaQueryFactory
                .select(
                        new QClubMemberResponse(
                        user.id,
                        user.nickname,
                        user.profileImage,
                        clubJoinEntry.createdAt
                        )
                )
                .from(clubJoinEntry)
                .leftJoin(user).on(clubJoinEntry.userId.eq(user.id))
                .where(clubJoinEntry.clubId.eq(clubId))
                .fetch();
    }

}
