package com.example.moyiza_be.event.dto;

import com.example.moyiza_be.event.entity.Event;
import lombok.Getter;

import java.util.Calendar;

@Getter
public class EventCreateResponseDto {
    private final String eventTitle;
    private final String eventContent;
    private final String eventLocation;
//    private Tag tag;
    private final Calendar eventStartTime;
    private final int eventGroupsize;

    // for image
//    private String image;

    // create
    public EventCreateResponseDto(Event event) {
        this.eventTitle = event.getEventTitle();
        this.eventContent = event.getEventContent();
        this.eventLocation = event.getEventLocation();
        this.eventStartTime = event.getStartTime();
        this.eventGroupsize = event.getEventGroupsize();
//        this.image = event.getImage();
    }
}
