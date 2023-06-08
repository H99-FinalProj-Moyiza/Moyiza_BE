package com.example.moyiza_be.oneday.dto;

import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.common.enums.OneDayTypeEnum;
import com.example.moyiza_be.common.enums.TagEnum;
import com.example.moyiza_be.oneday.entity.OneDay;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class OneDayNearByResponseDto {

    private Long oneDayId;
    private Long ownerName;
    private String oneDayTitle;
    private String oneDayContent;
    private String oneDayLocation;
    private double oneDayLatitude;
    private double oneDayLongitude;
    private LocalDateTime oneDayStartTime;
    private String oneDayCategory;
    private List<String> oneDayTag;
    private Integer agePolicy;
    private GenderPolicyEnum genderPolicy;
    private Integer oneDayGroupSize;
    private Integer attendantsNum;
    private String oneDayImage;
    private OneDayTypeEnum type;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private double distance;

    public OneDayNearByResponseDto(OneDay oneDay, double distance) {
        this.oneDayId = oneDay.getId();
        this.ownerName = oneDay.getOwnerId();
        this.oneDayTitle = oneDay.getOneDayTitle();
        this.oneDayContent = oneDay.getOneDayContent();
        this.oneDayLocation = oneDay.getOneDayLocation();
        this.oneDayLatitude = oneDay.getOneDayLatitude();
        this.oneDayLongitude = oneDay.getOneDayLongitude();
        this.oneDayStartTime = oneDay.getOneDayStartTime();
        this.oneDayCategory = oneDay.getCategory().getCategory();
        this.oneDayTag = TagEnum.parseTag(oneDay.getTagString());
        this.agePolicy = oneDay.getAgePolicy();
        this.genderPolicy = oneDay.getGenderPolicy();
        this.oneDayGroupSize = oneDay.getOneDayGroupSize();
        this.attendantsNum = oneDay.getAttendantsNum();
        this.oneDayImage = oneDay.getOneDayImage();
        this.type = oneDay.getType();
        this.createdAt = oneDay.getCreatedAt();
        this.modifiedAt = oneDay.getModifiedAt();
        this.distance = distance;
    }

}
