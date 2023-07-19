package com.example.moyiza_be.domain.club.repository.QueryDSL;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.moyiza_be.domain.club.entity.QClubImageUrl.clubImageUrl;

@Repository
@RequiredArgsConstructor
public class ClubImageUrlRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public List<String> getAllImageUrlByClubId(Long clubId) {
        return jpaQueryFactory
                .select(
                        clubImageUrl.imageUrl
                )
                .from(clubImageUrl)
                .where(clubImageUrl.clubId.eq(clubId))
                .fetch();
    }




}