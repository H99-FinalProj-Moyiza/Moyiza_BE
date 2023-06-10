package com.example.moyiza_be.event.dto;

import com.example.moyiza_be.event.entity.Event;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class EventSimpleDetailDto {
    private final Long id;
    private final Long ownerId;
    private final Long clubId;
    private final String eventTitle;
    private final String eventContent;
    private final String eventLocation;
    private final String eventLatitude;
    private final String eventLongitude;
    private final LocalDateTime eventStartTime;
    private final Integer eventGroupSize;
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
