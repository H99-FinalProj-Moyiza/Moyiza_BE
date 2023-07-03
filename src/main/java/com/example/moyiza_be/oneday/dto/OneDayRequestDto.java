package com.example.moyiza_be.oneday.dto;

import com.example.moyiza_be.common.enums.CategoryEnum;
import com.example.moyiza_be.common.enums.GenderPolicyEnum;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
public class OneDayRequestDto {
    private String oneDayTitle;
    private String oneDayContent;
    private CategoryEnum category;
    private String tagString;
    private String oneDayLocation;
    private double oneDayLatitude;
    private double oneDayLongitude;
    private int oneDayGroupSize;
    private LocalDateTime oneDayStartTime;
    private GenderPolicyEnum genderPolicy;
    private Integer agePolicy;
    private MultipartFile image;
}
