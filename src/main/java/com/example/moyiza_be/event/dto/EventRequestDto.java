package com.example.moyiza_be.event.dto;

import lombok.Getter;

import java.util.Calendar;

@Getter
public class EventRequestDto {
    private  String eventTitle;
    private String eventContent;
    private String location;
    private int eventGroupsize;
    private Calendar startTime;
}
