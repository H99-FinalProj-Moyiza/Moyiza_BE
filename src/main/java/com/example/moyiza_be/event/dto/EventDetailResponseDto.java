package com.example.moyiza_be.event.dto;

import com.example.moyiza_be.event.entity.Event;
import lombok.Getter;

import java.util.Calendar;

@Getter
public class EventDetailResponseDto {
    private long id;
    private String eventTitle;
    private String eventContent;
    private String eventLocation;
//    private Tag tag;
    private Calendar eventStartTime;
    private int eventGroupSize;
    private boolean deleted;
//    private String image;


}
