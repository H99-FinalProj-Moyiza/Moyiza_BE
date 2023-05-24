package com.example.moyiza_be.event.dto;

import com.example.moyiza_be.event.entity.EventAttendant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventAttendantResponseDto {
    private long attendantId;
    private long eventId;
    private long userId;
    private boolean entrance;

    public EventAttendantResponseDto(EventAttendant attendant) {
        this.attendantId = attendant.getId();
        this.eventId = attendant.getEvent().getId();
        this.userId = attendant.getUser().getId();
        this.entrance = attendant.isEntrance();
    }
}
