package com.example.moyiza_be.event.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

@Getter
public class EventRequestDto {
    private  String eventTitle;
    private String eventContent;
    private String eventLocation;
    private String eventLatitude;
    private String eventLongitude;
    private int eventGroupSize;
    private LocalDateTime eventStartTime;
//    private MultipartFile image;
}
