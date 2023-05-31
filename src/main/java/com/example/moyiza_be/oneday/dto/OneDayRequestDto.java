package com.example.moyiza_be.oneday.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Calendar;

@Getter
public class OneDayRequestDto {
    private String oneDayTitle;
    private String oneDayContent;
    private String oneDayLocation;
    private String oneDayLatitude;
    private String oneDayLongitude;
    private int oneDayGroupSize;
    private Calendar oneDayStartTime;
    private MultipartFile image;
}
