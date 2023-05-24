package com.example.moyiza_be.event.dto;

import com.example.moyiza_be.event.entity.Event;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Calendar;

@Getter
public class EventUpdateRequestDto {

    private final String eventTitle;
    private final String eventContent;
    private final Calendar eventStartTime;
    private final String eventLocation;
    private final int eventGroupsize;
//    private String image;
// 수정할 때 not null 메세지 있어야 하지 않을까
    public EventUpdateRequestDto(Event event) {
        this.eventTitle = event.getEventTitle();
        this.eventContent = event.getEventContent();
        this.eventStartTime = event.getEventStartTime();
        this.eventLocation = event.getEventLocation();
        this.eventGroupsize = event.getEventGroupSize();
//        this.image = event.getImage();
    }

}
