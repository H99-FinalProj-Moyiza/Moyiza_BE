package com.example.moyiza_be.oneday.dto;

import com.example.moyiza_be.event.entity.Event;
import com.example.moyiza_be.event.entity.EventAttendant;
import com.example.moyiza_be.oneday.entity.OneDay;
import com.example.moyiza_be.oneday.entity.OneDayAttendant;
import com.example.moyiza_be.oneday.repository.OneDayAttendantRepository;
import lombok.Getter;

import java.util.Calendar;
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
    private Calendar oneDayStartTime;
    private int oneDayGroupSize;
    private boolean deleted;
    //    private String image;
    private List<OneDayAttendant> oneDayAttendantList;
    private int oneDayAttendantListSize;
    public OneDayDetailResponseDto(OneDay oneDay, List<OneDayAttendant> attendantList, int people) {
        this.id = oneDay.getId();
        this.oneDayTitle = oneDay.getOneDayTitle();
        this.oneDayContent = oneDay.getOneDayContent();
        this.oneDayLocation = oneDay.getOneDayLocation();
        this.oneDayLatitude = oneDay.getOneDayLatitude();
        this.oneDayLongitude = oneDay.getOneDayLongitude();
        this.oneDayStartTime = oneDay.getOneDayStartTime();
        this.oneDayGroupSize = oneDay.getOneDayGroupSize();
        this.deleted = oneDay.isDeleted();
        this.oneDayAttendantList = attendantList;
        this.oneDayAttendantListSize = people;
    }
}
