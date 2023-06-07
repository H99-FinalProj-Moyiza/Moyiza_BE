package com.example.moyiza_be.oneday.dto.onedaycreate;

import com.example.moyiza_be.common.enums.*;
import com.example.moyiza_be.oneday.entity.OneDayCreate;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
public class OneDayCreatingResponseDto {
//    private long id;
    private String oneDayTitle;
    private String oneDayContent;
    private String oneDayLocation;
    private double oneDayLatitude;
    private double oneDayLongitude;
    private CategoryEnum category;
    private List<String> tag;
    private LocalDateTime oneDayStartTime;
    private int oneDayGroupSize;
    private GenderPolicyEnum gender;
    private Integer age;
    private OneDayTypeEnum type;
//    private boolean deleted;
    private String image;
//    private MultipartFile images;
    public OneDayCreatingResponseDto(OneDayCreate oneDay) {
        this.oneDayTitle = oneDay.getOneDayTitle();
        this.oneDayContent = oneDay.getOneDayContent();
        this.category = oneDay.getCategory();
        this.tag = Collections.singletonList(oneDay.getTagString());
        this.oneDayLocation = oneDay.getOneDayLocation();
        this.oneDayLatitude = oneDay.getOneDayLatitude();
        this.oneDayLongitude = oneDay.getOneDayLongitude();
        this.oneDayStartTime = oneDay.getOneDayStartTime();
        this.oneDayGroupSize = oneDay.getOneDayGroupSize();
        this.gender = oneDay.getGenderPolicy();
        this.age = oneDay.getAgePolicy();
        this.type = oneDay.getOneDayType();
        this.image = oneDay.getOneDayImage();
    }
}
