package com.example.moyiza_be.oneday.dto;

import com.example.moyiza_be.oneday.entity.OneDay;
import lombok.Getter;

@Getter
public class OneDayListResponseDto {
    private final Long oneDayId;
    private final String oneDayTitle;
    private final String oneDayContent;
    private final Integer oneDayGroupSize;
    private final Integer oneDayAttendantsNum;
    private final String oneDayLocation;

    public OneDayListResponseDto(OneDay oneDay, int size) {
        this.oneDayId = oneDay.getId();
        this.oneDayTitle = oneDay.getOneDayTitle();
        this.oneDayContent = oneDay.getOneDayContent();
        this.oneDayGroupSize = oneDay.getOneDayGroupSize();
        this.oneDayLocation = oneDay.getOneDayLocation();
        this.oneDayAttendantsNum = size;
    }
}
