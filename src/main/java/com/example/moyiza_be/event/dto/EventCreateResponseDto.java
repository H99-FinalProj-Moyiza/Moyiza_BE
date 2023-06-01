package com.example.moyiza_be.event.dto;

import com.example.moyiza_be.event.entity.Event;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

@Getter
public class EventCreateResponseDto {
    private final String eventTitle;
    private final String eventContent;
    private String eventLocation;
    private String eventLatitude;
    private String eventLongitude;
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
        this.eventLatitude = event.getEventLatitude();
        this.eventLongitude = event.getEventLongitude();
        this.eventStartTime = event.getEventStartTime();
        this.eventGroupSize = event.getEventGroupSize();
//        this.image = event.getImage();
    }
}
