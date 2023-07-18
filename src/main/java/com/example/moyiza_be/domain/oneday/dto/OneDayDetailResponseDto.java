package com.example.moyiza_be.domain.oneday.dto;

import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.domain.oneday.entity.OneDay;
import com.example.moyiza_be.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OneDayDetailResponseDto {
    private final long id;
    private final Long ownerId;
    private final String ownerNickname;
    private final String ownerProfileUrl;
    private final String oneDayTitle;
    private final String oneDayContent;
    private final String oneDayLocation;
    private final double oneDayLatitude;
    private final double oneDayLongitude;
    private final String genderPolicy;
    private final Integer agePolicy;
    private final String category;
    private final List<String> tagString;
    //    private Tag tag;
    private final LocalDateTime oneDayStartTime;
    private final int oneDayGroupSize;
    private final List<String> imageList;
    private final List<MemberResponse> memberResponseList;
    private final int oneDayAttendantListSize;
    private final String type;
    private final Integer numLikes;
    private final Boolean isLikedByUser;

    public OneDayDetailResponseDto(
            User owner, OneDay oneDay, List<String> oneDayImageUrlList,
            List<MemberResponse> memberResponseList, Boolean isLikedByUser
    ) {
        this.id = oneDay.getId();
        this.ownerId = owner.getId();
        this.ownerNickname = owner.getNickname();
        this.ownerProfileUrl = owner.getProfileImage();
        this.oneDayTitle = oneDay.getOneDayTitle();
        this.oneDayContent = oneDay.getOneDayContent();
        this.category = oneDay.getCategory().getCategory();
        this.tagString = TagEnum.parseTag(oneDay.getTagString());
        this.oneDayLocation = oneDay.getOneDayLocation();
        this.oneDayLatitude = oneDay.getOneDayLatitude();
        this.oneDayLongitude = oneDay.getOneDayLongitude();
        this.genderPolicy = oneDay.getGenderPolicy().getGenderPolicy();
        this.agePolicy = oneDay.getAgePolicy();
        this.oneDayStartTime = oneDay.getOneDayStartTime();
        this.oneDayGroupSize = oneDay.getOneDayGroupSize();
        this.type = oneDay.getType().getOneDayType();
        this.imageList = oneDayImageUrlList;
        this.memberResponseList = memberResponseList;
        this.oneDayAttendantListSize = memberResponseList.size();
        this.numLikes = oneDay.getNumLikes();
        this.isLikedByUser = isLikedByUser;
    }
}
