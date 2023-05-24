package com.example.moyiza_be.event.dto;

import com.example.moyiza_be.event.entity.Event;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Calendar;

@Getter
public class EventCreateResponseDto {
    private final String eventTitle;
    private final String eventContent;
    private final String eventLocation;
//    private Tag tag;
    private final LocalDateTime eventStartTime;
    private final int eventGroupSize;

    // for image
//    private String image;

    // create
    public EventCreateResponseDto(Event event) {
        this.eventTitle = event.getEventTitle();
        this.eventContent = event.getEventContent();
        this.eventLocation = event.getEventLocation();
        this.eventStartTime = event.getEventStartTime();
        this.eventGroupSize = event.getEventGroupSize();
//        this.image = event.getImage();
    }
}
