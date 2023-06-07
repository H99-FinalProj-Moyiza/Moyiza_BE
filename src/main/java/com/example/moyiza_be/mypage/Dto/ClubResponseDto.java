package com.example.moyiza_be.mypage.Dto;

import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.common.enums.TagEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ClubResponseDto {
    private Long club_id;
    private String clubCategory;
    private List<String> clubTag;
    private String clubTitle;
    private String clubContent;
    private Integer nowMemberCount;
    private Integer maxGroupSize;
    private String thumbnailUrl;

    public ClubResponseDto(Club club) {
        this.club_id = club.getId();
        this.clubCategory = club.getCategory().getCategory();
        this.clubTag = TagEnum.parseTag(club.getTagString());
        this.clubTitle = club.getTitle();
        this.clubContent = club.getContent();
        this.nowMemberCount = club.getNowMemberCount();
        this.maxGroupSize = club.getMaxGroupSize();
        this.thumbnailUrl = club.getThumbnailUrl();
    }
}
