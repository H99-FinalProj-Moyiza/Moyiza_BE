package com.example.moyiza_be.oneday.dto;

import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.oneday.entity.OneDay;
import com.example.moyiza_be.oneday.entity.OneDayImageUrl;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
    private GenderPolicyEnum genderPolicy;
    private Integer oneDayGroupSize;
    private Integer attendantsNum;
    private String oneDayImage;
    private List<String> oneDayImageUrlList;

    public OneDayDetailResponse(OneDay oneDay, List<OneDayImageUrl> oneDayImageUrl) {
        this.oneDayId = oneDay.getId();
        this.ownerName = oneDay.getOwnerId();
        this.oneDayTitle = oneDay.getOneDayTitle();
        this.oneDayCategory = oneDay.getCategory().getCategory();
        this.oneDayTag = TagEnum.parseTag(oneDay.getTagString());
        this.oneDayContent = oneDay.getOneDayContent();
        this.agePolicy = oneDay.getAgePolicy();
        this.genderPolicy = oneDay.getGenderPolicy();
        this.oneDayGroupSize = oneDay.getOneDayGroupSize();
        this.attendantsNum = 1;
        this.oneDayImage = oneDay.getOneDayImage();
        this.oneDayImageUrlList = oneDayImageUrl.stream().map(OneDayImageUrl::getImageUrl).collect(Collectors.toList());
    }
}
