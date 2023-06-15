package com.example.moyiza_be.club.dto;

import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.user.entity.User;
import com.querydsl.core.annotations.QueryProjection;
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
    private Integer numLikes;
    private Boolean isLikedByUser;
    private List<ClubMemberResponse> memberList;


    public ClubDetailResponse(Club club, List<String> clubImageUrlList, User user) {
        this.club_id = club.getId();
        this.ownerNickname = user.getNickname();
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
        this.numLikes = 0;
        this.isLikedByUser = false;
    }

    @QueryProjection
    public ClubDetailResponse(
            Long club_id, String ownerNickname, String clubTitle, CategoryEnum categoryEnum, String tagString,
            String clubContent, Integer agePolicy, GenderPolicyEnum genderPolicyEnum, Integer maxGroupSize,
            Integer nowMemberCount, String thumbnailUrl, Integer numLikes, Boolean isLikedByUser) {
        this.club_id = club_id;
        this.ownerNickname = ownerNickname;
        this.clubTitle = clubTitle;
        this.clubCategory = categoryEnum.getCategory();
        this.clubTag = TagEnum.parseTag(tagString);
        this.clubContent = clubContent;
        this.agePolicy = agePolicy;
        this.genderPolicy = genderPolicyEnum.getGenderPolicy();
        this.maxGroupSize = maxGroupSize;
        this.nowMemberCount = nowMemberCount;
        this.thumbnailUrl = thumbnailUrl;
        this.numLikes = numLikes;
        this.isLikedByUser = isLikedByUser;
//        this.clubImageUrlList = clubImageUrlList; //Difficult to handle with queries
    }
    public void setClubImageUrlList(List<String> clubImageUrlList) {
        this.clubImageUrlList = clubImageUrlList;
    }
    public void setMemberList(List<ClubMemberResponse> memberList) { this.memberList = memberList;}
}
