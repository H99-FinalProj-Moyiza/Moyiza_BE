package com.example.moyiza_be.oneday.dto;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import com.example.moyiza_be.oneday.entity.OneDay;
import jakarta.persistence.Column;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Calendar;

@Getter
@NoArgsConstructor
public class OneDayUpdateRequestDto {
    private String oneDayTitle;
    private String oneDayContent;
    private CategoryEnum category;
    private String tagString;
    private GenderPolicyEnum genderPolicy;
    private Integer agePolicy;
    private LocalDateTime oneDayStartTime;
    private String oneDayLocation;
    private double oneDayLatitude;
    private double oneDayLongitude;
    private int oneDayGroupSize;

//    public OneDayUpdateRequestDto(OneDay oneDay) {
//        this.oneDayTitle = oneDay.getOneDayTitle();
//        this.oneDayContent = oneDay.getOneDayContent();
//        this.category = oneDay.getCategory();
//        this.tagString = oneDay.getTagString();
//        this.agePolicy = oneDay.getAgePolicy();
//        this.genderPolicy = oneDay.getGenderPolicy();
//        this.oneDayStartTime = oneDay.getOneDayStartTime();
//        this.oneDayLocation = oneDay.getOneDayLocation();
//        this.oneDayLatitude = oneDay.getOneDayLatitude();
//        this.oneDayLongitude = oneDay.getOneDayLongitude();
//        this.oneDayGroupSize = oneDay.getOneDayGroupSize();
//    }

}
