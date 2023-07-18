package com.example.moyiza_be.domain.club.dto;

import com.example.moyiza_be.domain.club.entity.Club;
import com.example.moyiza_be.common.enums.TagEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ClubSimpleResponseDto {
    private Long club_id;
    private String clubTitle;
    private String clubCategory;
    private List<String> clubTag;
    private String clubContent;
    private Integer maxGroupSize;
    private Integer nowMemberCount;
    private List<String> clubImageUrlList;
    private Integer numLikes;


    public ClubSimpleResponseDto(Club club, List<String> clubImageUrlList) {
        this.club_id = club.getId();
        this.clubTitle = club.getTitle();
        this.clubCategory = club.getCategory().getCategory();
        this.clubTag = TagEnum.parseTag(club.getTagString());
        this.clubContent = club.getContent();
        this.maxGroupSize = club.getMaxGroupSize();
        this.nowMemberCount = club.getNowMemberCount();
        this.clubImageUrlList = clubImageUrlList;
        this.numLikes = club.getNumLikes();
    }

}
