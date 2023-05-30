package com.example.moyiza_be.event.dto;

import lombok.Data;

@Data
public class EventDto {
    private long id;
    private String eventTitle;
    private String eventContent;
    private String eventLocation;
    private String eventLatitude;
    private String eventLongitude;
    private long ownerId;
}
