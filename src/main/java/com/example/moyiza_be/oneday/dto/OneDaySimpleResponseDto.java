package com.example.moyiza_be.oneday.dto;

import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.oneday.entity.OneDay;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

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
    private String  oneDayImage;
    private Integer numLikes;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public OneDaySimpleResponseDto(OneDay oneDay) {
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
        this.oneDayImage = oneDay.getOneDayImage();
        this.createdAt = oneDay.getCreatedAt();
        this.modifiedAt = oneDay.getModifiedAt();
        this.numLikes = oneDay.getNumLikes();
    }
}
