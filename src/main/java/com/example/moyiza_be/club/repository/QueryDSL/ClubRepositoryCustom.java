package com.example.moyiza_be.club.repository.QueryDSL;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import static com.example.moyiza_be.club.entity.QClub.club;

@Repository
@RequiredArgsConstructor
public class ClubRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;


}
