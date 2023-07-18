package com.example.moyiza_be.domain.event.dto;

import com.example.moyiza_be.domain.event.entity.Event;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EventUpdateRequestDto {

    private final String eventTitle;
    private final String eventContent;
    private final LocalDateTime eventStartTime;
    private final String eventLocation;
    private final String eventLatitude;
    private final String eventLongitude;
    private final int eventGroupSize;
//    private String image;
    public EventUpdateRequestDto(Event event) {
        this.eventTitle = event.getEventTitle();
        this.eventContent = event.getEventContent();
        this.eventStartTime = event.getEventStartTime();
        this.eventLocation = event.getEventLocation();
        this.eventLatitude = event.getEventLatitude();
        this.eventLongitude = event.getEventLongitude();
        this.eventGroupSize = event.getEventGroupSize();
//        this.image = event.getImage();
    }

}
