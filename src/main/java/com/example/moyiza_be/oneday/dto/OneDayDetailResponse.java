package com.example.moyiza_be.oneday.dto;

import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.oneday.entity.OneDay;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OneDayDetailResponse {
    private Long oneDayId;
    private Long ownerName;
    private String oneDayTitle;
    private String oneDayCategory;
    private List<String> oneDayTag;
    private String oneDayContent;
    private Integer agePolicy;
    private String genderPolicy;
    private Integer oneDayGroupSize;
    private Integer attendantsNum;
    private String oneDayImage;
    private List<String> oneDayImageUrlList;

    public OneDayDetailResponse(OneDay oneDay, List<String> oneDayImageUrl) {
        this.oneDayId = oneDay.getId();
        this.ownerName = oneDay.getOwnerId();
        this.oneDayTitle = oneDay.getOneDayTitle();
        this.oneDayCategory = oneDay.getCategory().getCategory();
        this.oneDayTag = TagEnum.parseTag(oneDay.getTagString());
        this.oneDayContent = oneDay.getOneDayContent();
        this.agePolicy = oneDay.getAgePolicy();
        this.genderPolicy = oneDay.getGenderPolicy().getGenderPolicy();
        this.oneDayGroupSize = oneDay.getOneDayGroupSize();
        this.attendantsNum = 1;
        this.oneDayImage = oneDay.getOneDayImage();
        this.oneDayImageUrlList = oneDayImageUrl;
    }
}
