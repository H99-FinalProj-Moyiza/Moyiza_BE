package com.example.moyiza_be.mypage.Dto;

import com.example.moyiza_be.club.entity.Club;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ClubResponseDto {
    private Long club_id;
    private String clubCategory;
    private String clubTag;
    private String clubTitle;
    private Integer nowMemberCount;
    private String thumbnailUrl;

    public ClubResponseDto(Club club) {
        this.club_id = club.getId();
        this.clubCategory = club.getCategory().getCategory();
        this.clubTag = club.getTagString();
        this.clubTitle = club.getTitle();
        this.nowMemberCount = club.getNowMemberCount();
        this.thumbnailUrl = club.getThumbnailUrl();
    }
}
