package com.example.moyiza_be.domain.oneday.dto.onedaycreate;

import com.example.moyiza_be.common.enums.*;
import com.example.moyiza_be.domain.oneday.entity.OneDayCreate;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OneDayCreatingResponseDto {
//    private long id;
    private final String oneDayTitle;
    private final String oneDayContent;
    private final String oneDayLocation;
    private final double oneDayLatitude;
    private final double oneDayLongitude;
    private final String category;
    private final List<String> tag;
    private final LocalDateTime oneDayStartTime;
    private final Integer oneDayGroupSize;
    private final String gender;
    private final Integer age;
    private final String type;
//    private boolean deleted;
    private final String image;
//    private MultipartFile images;
    public OneDayCreatingResponseDto(OneDayCreate oneDay) {
        this.oneDayTitle = oneDay.getOneDayTitle();
        this.oneDayContent = oneDay.getOneDayContent();
        this.category = (oneDay.getCategory() != null) ? oneDay.getCategory().getCategory() : null;
        this.tag = TagEnum.parseTag(oneDay.getTagString());
        this.oneDayLocation = oneDay.getOneDayLocation();
        this.oneDayLatitude = oneDay.getOneDayLatitude();
        this.oneDayLongitude = oneDay.getOneDayLongitude();
        this.oneDayStartTime = oneDay.getOneDayStartTime();
        this.oneDayGroupSize = oneDay.getOneDayGroupSize();
        this.gender = (oneDay.getGenderPolicy() != null) ? oneDay.getGenderPolicy().getGenderPolicy() : null;
        this.age = oneDay.getAgePolicy();
        this.type = (oneDay.getOneDayType() != null) ? oneDay.getOneDayType().getOneDayType() : null;
        this.image = oneDay.getOneDayImage();
    }
}
