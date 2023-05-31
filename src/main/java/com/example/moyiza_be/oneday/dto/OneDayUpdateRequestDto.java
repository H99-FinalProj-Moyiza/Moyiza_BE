package com.example.moyiza_be.oneday.dto;

import com.example.moyiza_be.oneday.entity.OneDay;
import lombok.Getter;

import java.util.Calendar;

@Getter
public class OneDayUpdateRequestDto {
    private final String oneDayTitle;
    private final String oneDayContent;
    private final Calendar oneDayStartTime;
    private final String oneDayLocation;
    private final String oneDayLatitude;
    private final String oneDayLongitude;
    private final int oneDayGroupSize;
//    private String image;
    public OneDayUpdateRequestDto(OneDay oneDay) {
        this.oneDayTitle = oneDay.getOneDayTitle();
        this.oneDayContent = oneDay.getOneDayContent();
        this.oneDayStartTime = oneDay.getOneDayStartTime();
        this.oneDayLocation = oneDay.getOneDayLocation();
        this.oneDayLatitude = oneDay.getOneDayLatitude();
        this.oneDayLongitude = oneDay.getOneDayLongitude();
        this.oneDayGroupSize = oneDay.getOneDayGroupSize();
    }

}
