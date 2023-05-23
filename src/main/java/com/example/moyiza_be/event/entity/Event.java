package com.example.moyiza_be.event.entity;

import com.example.moyiza_be.common.utils.TimeStamped;
import com.example.moyiza_be.event.dto.EventRequestDto;
import com.example.moyiza_be.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Event extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userId")
//    private User ownerId;

    @Column(nullable = false)
    private String eventTitle;

    @Column(nullable = false)
    private String eventContent;

    @Column
    private String eventLocation;

    @Column
    private Calendar startTime;

    @Column
    private int eventGroupsize;


    public Event(EventRequestDto eventRequestDto, User user) {
        this.eventTitle = eventRequestDto.getEventTitle();
        this.eventContent = eventRequestDto.getEventContent();
        this.eventLocation = eventRequestDto.getLocation();
        this.eventGroupsize = eventRequestDto.getEventGroupsize();
        this.startTime = eventRequestDto.getStartTime();
//        super(); 를 쓸 수 있지 않을까?
    }

    public void deleteEvent() {
        this.deleted = true;
    }
}