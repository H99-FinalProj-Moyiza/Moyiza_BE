package com.example.moyiza_be.club.dto;

import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.common.enums.TagEnum;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;

@Getter
public class ClubListResponse {
    private final Long club_id;
    private final String ownerNickname;
    private final String clubTitle;
    private final List<String> clubTag;
    private final Integer maxGroupSize;
    private final Integer nowMemberCount;
    private final String thumbnailUrl;
    private final Integer numLikes;
    private final Boolean isLikedByUser;

    @QueryProjection
    public ClubListResponse(Long club_id, String ownerNickname, String clubTitle, String tagString, Integer maxGroupSize,
                            Integer nowMemberCount, String thumbnailUrl, Integer numLikes, Boolean isLikedByUser) {
        this.club_id = club_id;
        this.ownerNickname = ownerNickname;
        this.clubTitle = clubTitle;
        this.clubTag = TagEnum.parseTag(tagString);
        this.maxGroupSize = maxGroupSize;
        this.nowMemberCount = nowMemberCount;
        this.thumbnailUrl = thumbnailUrl;
        this.numLikes = numLikes;
        this.isLikedByUser = isLikedByUser;
    }

}
