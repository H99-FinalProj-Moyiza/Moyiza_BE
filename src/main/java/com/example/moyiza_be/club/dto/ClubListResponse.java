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
    public ClubListResponse(Club club) {
        this.club_id = club.getId();
        this.ownerNickname = "추후반영예정";
        this.clubTitle = club.getTitle();
        this.clubTag = TagEnum.parseTag(club.getTagString());
        this.maxGroupSize = club.getMaxGroupSize();
        this.nowMemberCount = club.getNowMemberCount();   // queryDSL 이후 수정
        this.thumbnailUrl = club.getThumbnailUrl();
    }

    @QueryProjection
    public ClubListResponse(Long club_id, String ownerNickname, String clubTitle, String tagString, Integer maxGroupSize,
                            Integer nowMemberCount, String thumbnailUrl) {
        this.club_id = club_id;
        this.ownerNickname = ownerNickname;
        this.clubTitle = clubTitle;
        this.clubTag = TagEnum.parseTag(tagString);
        this.maxGroupSize = maxGroupSize;
        this.nowMemberCount = nowMemberCount;
//        this.thumbnailUrl = thumbnailUrl.get(0);
        this.thumbnailUrl = thumbnailUrl;
    }

}
