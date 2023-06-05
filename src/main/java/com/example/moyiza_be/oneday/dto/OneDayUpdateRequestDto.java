package com.example.moyiza_be.oneday.dto;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.oneday.entity.OneDay;
import jakarta.persistence.Column;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Calendar;

@Getter
public class OneDayUpdateRequestDto {
    private final String oneDayTitle;
    private final String oneDayContent;
    private final CategoryEnum category;
    private final String tagString;
    private final GenderPolicyEnum genderPolicy;
    private final Integer agePolicy;
    private final LocalDateTime oneDayStartTime;
    private final String oneDayLocation;
    private final String oneDayLatitude;
    private final String oneDayLongitude;
    private final int oneDayGroupSize;
    private MultipartFile image;
    public OneDayUpdateRequestDto(OneDay oneDay, MultipartFile image) {
        this.oneDayTitle = oneDay.getOneDayTitle();
        this.oneDayContent = oneDay.getOneDayContent();
        this.category = oneDay.getCategory();
        this.tagString = oneDay.getTagString();
        this.agePolicy = oneDay.getAgePolicy();
        this.genderPolicy = oneDay.getGenderPolicy();
        this.oneDayStartTime = oneDay.getOneDayStartTime();
        this.oneDayLocation = oneDay.getOneDayLocation();
        this.oneDayLatitude = oneDay.getOneDayLatitude();
        this.oneDayLongitude = oneDay.getOneDayLongitude();
        this.oneDayGroupSize = oneDay.getOneDayGroupSize();
        this.image = image;
    }

}
