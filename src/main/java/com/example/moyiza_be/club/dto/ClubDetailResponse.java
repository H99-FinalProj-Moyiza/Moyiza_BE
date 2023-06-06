package com.example.moyiza_be.club.dto;

import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.common.enums.TagEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ClubDetailResponse {
    private Long club_id;
    private String ownerNickname;
    private String clubTitle;
    private String clubCategory;
    private List<String> clubTag;
    private String clubContent;
    private Integer agePolicy;
    private String genderPolicy;
    private Integer maxGroupSize;
    private Integer nowMemberCount;
    private String thumbnailUrl;
    private List<String> clubImageUrlList;

    public ClubDetailResponse(Club club, List<String> clubImageUrlList) {
        this.club_id = club.getId();
        this.ownerNickname = "추후반영예정";    // 쿼리 수정할 때 닉네임 가져오게하기
        this.clubTitle = club.getTitle();
        this.clubCategory = club.getCategory().getCategory();
        this.clubTag = TagEnum.parseTag(club.getTagString());
        this.clubContent = club.getContent();
        this.agePolicy = club.getAgePolicy();
        this.genderPolicy = club.getGenderPolicy().getGenderPolicy();
        this.maxGroupSize = club.getMaxGroupSize();
        this.nowMemberCount = club.getNowMemberCount();
        this.thumbnailUrl = club.getThumbnailUrl();
        this.clubImageUrlList = clubImageUrlList;  //  쿼리한방에 가능한가 ?
    }

}
