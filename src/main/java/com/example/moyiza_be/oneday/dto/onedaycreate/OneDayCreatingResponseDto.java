package com.example.moyiza_be.oneday.dto.onedaycreate;

import com.example.moyiza_be.oneday.entity.OneDayCreate;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OneDayCreatingResponseDto {
//    private long id;
    private String oneDayTitle;
    private String oneDayContent;
    private String oneDayLocation;
    private double oneDayLatitude;
    private double oneDayLongitude;
    //    private Tag tag;
    private LocalDateTime oneDayStartTime;
    private int oneDayGroupSize;
//    private boolean deleted;
    private String image;
//    private MultipartFile images;
    public OneDayCreatingResponseDto(OneDayCreate oneDay) {
        this.oneDayTitle = oneDay.getOneDayTitle();
        this.oneDayContent = oneDay.getOneDayContent();
        this.oneDayLocation = oneDay.getOneDayLocation();
        this.oneDayLatitude = oneDay.getOneDayLatitude();
        this.oneDayLongitude = oneDay.getOneDayLongitude();
        this.oneDayStartTime = oneDay.getOneDayStartTime();
        this.oneDayGroupSize = oneDay.getOneDayGroupSize();
        this.image = oneDay.getOneDayImage();
//        this.images = fileUrl;
    }
}
