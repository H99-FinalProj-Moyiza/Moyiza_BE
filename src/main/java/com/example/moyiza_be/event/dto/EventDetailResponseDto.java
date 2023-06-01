package com.example.moyiza_be.event.dto;

import com.example.moyiza_be.event.entity.Event;
import com.example.moyiza_be.event.entity.EventAttendant;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;

@Getter
public class EventDetailResponseDto {
    private long id;
    private String eventTitle;
    private String eventContent;
    private String eventLocation;
    private String eventLatitude;
    private String eventLongitude;
//    private Tag tag;
    private LocalDateTime eventStartTime;
    private int eventGroupSize;
    private boolean deleted;
//    private String image;
    private List<EventAttendant> eventAttendantList;
    private int eventAttendantListSize;
    public EventDetailResponseDto(Event event, List<EventAttendant> attendantList, int people) {
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
    }
}
