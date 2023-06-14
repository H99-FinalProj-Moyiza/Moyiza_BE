package com.example.moyiza_be.event.dto;

import com.example.moyiza_be.event.entity.Event;
import com.example.moyiza_be.event.entity.EventAttendant;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class EventDetailResponseDto {
    private final long id;
    private final String eventTitle;
    private final String eventContent;
    private final String eventLocation;
    private final String eventLatitude;
    private final String eventLongitude;
//    private Tag tag;
    private final LocalDateTime eventStartTime;
    private final int eventGroupSize;
    private final boolean deleted;
//    private String image;
    private final List<EventAttendant> eventAttendantList;
    private final int eventAttendantListSize;
    private final Integer numLikes;
    private final Boolean isLikedByUser;
    public EventDetailResponseDto(
            Event event, List<EventAttendant> attendantList, int people, Boolean isLikedByUser
    ) {
        this.id = event.getId();
        this.eventTitle = event.getEventTitle();
        this.eventContent = event.getEventContent();
        this.eventLocation = event.getEventLocation();
        this.eventLatitude = event.getEventLatitude();
        this.eventLongitude = event.getEventLongitude();
        this.eventStartTime = event.getEventStartTime();
        this.eventGroupSize = event.getEventGroupSize();
        this.deleted = event.isDeleted();
        this.eventAttendantList = attendantList;
        this.eventAttendantListSize = people;
        this.numLikes = event.getNumLikes();
        this.isLikedByUser = isLikedByUser;
    }
}
