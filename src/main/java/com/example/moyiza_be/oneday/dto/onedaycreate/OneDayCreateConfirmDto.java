package com.example.moyiza_be.oneday.dto.onedaycreate;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.common.enums.OneDayTypeEnum;
import com.example.moyiza_be.oneday.entity.OneDayCreate;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Calendar;

@Getter
public class OneDayCreateConfirmDto {
    private final Long ownerId;
    private final CategoryEnum category;
    private final String tagString;
    private final String oneDayTitle;
    private final String oneDayContent;
    private final Integer oneDayGroupSize;
    private final GenderPolicyEnum genderPolicy;
    private final Integer agePolicy;

    private final Long createOneDayId;
    private final String oneDayImage;

    private final String oneDayLocation;
    private final double oneDayLatitude;
    private final double oneDayLongitude;
    private final LocalDateTime oneDayStartTime;
    public OneDayTypeEnum getOneDayType;

    public OneDayCreateConfirmDto(OneDayCreate oneDayCreate) {
        this.ownerId = oneDayCreate.getOwnerId();
        this.category = oneDayCreate.getCategory();
        this.tagString = oneDayCreate.getTagString();
        this.oneDayTitle = oneDayCreate.getOneDayTitle();
        this.oneDayContent = oneDayCreate.getOneDayContent();
        this.oneDayGroupSize = oneDayCreate.getOneDayGroupSize();
        this.genderPolicy = oneDayCreate.getGenderPolicy();
        this.agePolicy = oneDayCreate.getAgePolicy();
        this.createOneDayId = oneDayCreate.getId();
        this.oneDayImage = oneDayCreate.getOneDayImage();
        this.oneDayLocation = oneDayCreate.getOneDayLocation();
        this.oneDayLatitude = oneDayCreate.getOneDayLatitude();
        this.oneDayLongitude = oneDayCreate.getOneDayLongitude();
        this.oneDayStartTime = oneDayCreate.getOneDayStartTime();
        this.getOneDayType = oneDayCreate.getOneDayType();
    }

}
