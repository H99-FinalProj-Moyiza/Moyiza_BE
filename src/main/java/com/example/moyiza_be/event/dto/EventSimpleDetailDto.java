package com.example.moyiza_be.event.dto;

import com.example.moyiza_be.event.entity.Event;

import java.time.LocalDateTime;

public class EventSimpleDetailDto {
    private Long id;
    private Long ownerId;
    private Long clubId;
    private String eventTitle;
    private String eventContent;
    private String eventLocation;
    private String eventLatitude;
    private String eventLongitude;
    private LocalDateTime eventStartTime;
    private Integer eventGroupSize;

    private final int attendantsNum;
    public EventSimpleDetailDto(Event event, int size) {
        this.id = event.getId();
        this.ownerId = event.getOwnerId();
        this.clubId = event.getClubId();
        this.eventTitle = event.getEventTitle();
        this.eventContent = event.getEventContent();
        this.eventLocation = event.getEventLocation();
        this.eventLatitude = event.getEventLatitude();
        this.eventLongitude = event.getEventLongitude();
        this.eventStartTime = event.getEventStartTime();
        this.eventGroupSize = event.getEventGroupSize();
        this.attendantsNum = size;
    }
}
