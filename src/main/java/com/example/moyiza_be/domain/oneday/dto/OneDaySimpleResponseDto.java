package com.example.moyiza_be.domain.oneday.dto;

import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.domain.oneday.entity.OneDay;
import com.example.moyiza_be.domain.oneday.entity.OneDayImageUrl;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class OneDaySimpleResponseDto {
    private Long oneDayId;
    private Long ownerId;
    private String oneDayTitle;
    private String oneDayContent;
    private String oneDayCategory;
    private List<String> oneDayTag;
    private String oneDayLocation;
    private double oneDayLatitude;
    private double oneDayLongitude;
    private LocalDateTime oneDayStartTime;
    private Integer oneDayGroupSize;
    private Integer attendantsNum;
    private Integer numLikes;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<String> oneDayImageUrlList;

    public OneDaySimpleResponseDto(OneDay oneDay, List<OneDayImageUrl> oneDayImageUrlList) {
        this.oneDayId = oneDay.getId();
        this.ownerId = oneDay.getOwnerId();
        this.oneDayTitle = oneDay.getOneDayTitle();
        this.oneDayContent = oneDay.getOneDayContent();
        this.oneDayLocation = oneDay.getOneDayLocation();
        this.oneDayLatitude = oneDay.getOneDayLatitude();
        this.oneDayLongitude = oneDay.getOneDayLongitude();
        this.oneDayStartTime = oneDay.getOneDayStartTime();
        this.oneDayCategory = oneDay.getCategory().getCategory();
        this.oneDayTag = TagEnum.parseTag(oneDay.getTagString());
        this.oneDayGroupSize = oneDay.getOneDayGroupSize();
        this.attendantsNum = oneDay.getAttendantsNum();
        this.oneDayImageUrlList = oneDayImageUrlList.stream().map(OneDayImageUrl::getImageUrl).collect(Collectors.toList());
        this.createdAt = oneDay.getCreatedAt();
        this.modifiedAt = oneDay.getModifiedAt();
        this.numLikes = oneDay.getNumLikes();
    }
}
