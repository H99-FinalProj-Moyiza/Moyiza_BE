package com.example.moyiza_be.oneday.dto;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Calendar;

@Getter
public class OneDayRequestDto {
    private String oneDayTitle;
    private String oneDayContent;
    private CategoryEnum category;
    private String tagString;
    private String oneDayLocation;
    private String oneDayLatitude;
    private String oneDayLongitude;
    private int oneDayGroupSize;
    private Calendar oneDayStartTime;
    private GenderPolicyEnum genderPolicy;
    private Integer agePolicy;
    private MultipartFile image;
}
