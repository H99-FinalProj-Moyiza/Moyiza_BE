package com.example.moyiza_be.club.dto;

import com.example.moyiza_be.club.entity.Club;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ClubListResponse {
    private final Long club_id;
    private final String ownerNickname;
    private final String clubTitle;
    private final String tagString; // tag 정해지면 수정
    private final Integer maxGroupSize;
    private final Integer nowMemberCount;
    private final String thumbnailUrl;
    public ClubListResponse(Club club) {
        this.club_id = club.getId();
        this.ownerNickname = "추후반영예정";
        this.clubTitle = club.getTitle();
        this.tagString = club.getTagString();
        this.maxGroupSize = club.getMaxGroupSize();
        this.nowMemberCount = 11111;   // queryDSL 이후 수정
        this.thumbnailUrl = club.getThumbnailUrl();
    }
}
