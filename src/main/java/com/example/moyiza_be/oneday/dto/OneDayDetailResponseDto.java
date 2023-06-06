package com.example.moyiza_be.oneday.dto;

import com.example.moyiza_be.oneday.entity.OneDay;
import com.example.moyiza_be.oneday.entity.OneDayAttendant;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OneDayDetailResponseDto {
    private long id;
    private String oneDayTitle;
    private String oneDayContent;
    private String oneDayLocation;
    private String oneDayLatitude;
    private String oneDayLongitude;
    //    private Tag tag;
    private LocalDateTime oneDayStartTime;
    private int oneDayGroupSize;
    private boolean deleted;
    private List<String> imageList;
    private List<OneDayAttendant> oneDayAttendantList;
    private int oneDayAttendantListSize;
    public OneDayDetailResponseDto(OneDay oneDay,List<String> oneDayImageUrlList, List<OneDayAttendant> attendantList, Integer people) {
        this.id = oneDay.getId();
        this.oneDayTitle = oneDay.getOneDayTitle();
        this.oneDayContent = oneDay.getOneDayContent();
        this.oneDayLocation = oneDay.getOneDayLocation();
        this.oneDayLatitude = oneDay.getOneDayLatitude();
        this.oneDayLongitude = oneDay.getOneDayLongitude();
        this.oneDayStartTime = oneDay.getOneDayStartTime();
        this.oneDayGroupSize = oneDay.getOneDayGroupSize();
        this.deleted = oneDay.getDeleted();
        this.imageList = oneDayImageUrlList;
        this.oneDayAttendantList = attendantList;
        this.oneDayAttendantListSize = people;
    }
}
