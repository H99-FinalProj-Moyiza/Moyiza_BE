package com.example.moyiza_be.event.dto;

import com.example.moyiza_be.event.entity.Event;

public class EventSimpleDetailDto {
    private final int attendantsNum;
    public EventSimpleDetailDto(Event event, int size) {
        this.attendantsNum = size;
    }
}
