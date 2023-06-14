package com.example.moyiza_be.event.dto;

import com.example.moyiza_be.event.entity.Event;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class EventSimpleDetailDto {
    private final Long id;
    private final String ownerNickname;
    private final Long clubId;
    private final String eventTitle;
    private final String eventContent;
    private final String eventLocation;
    private final String eventLatitude;
    private final String eventLongitude;
    private final LocalDateTime eventStartTime;
    private final Integer eventGroupSize;
    private final int attendantsNum;
    private final String image;
    private final Integer numLikes;
    private final Boolean isLikedByUser;

    @QueryProjection
    public EventSimpleDetailDto(
            Long id, String ownerNickname, Long clubId, String eventTitle, String eventContent, String eventLocation,
            String eventLatitude, String eventLongitude, LocalDateTime eventStartTime,
            Integer eventGroupSize, int attendantsNum, String image, Integer numLikes, Boolean isLikedByUser) {
        this.id = id;
        this.ownerNickname = ownerNickname;
        this.clubId = clubId;
        this.eventTitle = eventTitle;
        this.eventContent = eventContent;
        this.eventLocation = eventLocation;
        this.eventLatitude = eventLatitude;
        this.eventLongitude = eventLongitude;
        this.eventStartTime = eventStartTime;
        this.eventGroupSize = eventGroupSize;
        this.attendantsNum = attendantsNum;
        this.image = image;
        this.numLikes = numLikes;
        this.isLikedByUser = isLikedByUser;
    }
}
