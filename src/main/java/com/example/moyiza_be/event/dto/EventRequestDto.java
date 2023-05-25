package com.example.moyiza_be.event.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Calendar;

@Getter
public class EventRequestDto {
    private  String eventTitle;
    private String eventContent;
    private String eventLocation;
    private int eventGroupSize;
    private Calendar eventStartTime;
}
