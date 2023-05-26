package com.example.moyiza_be.event.entity;

import com.example.moyiza_be.club.entity.Club;
import com.example.moyiza_be.common.utils.TimeStamped;
import com.example.moyiza_be.event.dto.EventRequestDto;
import com.example.moyiza_be.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Event extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userId")
//    private User ownerId;
    @Column(nullable = false)
    private long ownerId;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "clubId")
//    private Club clubId;
    @Column(nullable = false)
    private long clubId;

    @Column(nullable = false)
    private String eventTitle;

    @Column(nullable = false)
    private String eventContent;

    @Column
    private String eventLocation;

    @Column
    private Calendar eventStartTime;

    @Column
    private int eventGroupSize;

    @Column
    private boolean deleted;

    @Column
    private int attendantsNum;
    public void addAttend(){
        attendantsNum++;
    }

    public void cancelAttend(){
        attendantsNum--;
    }

//    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
//    private ArrayList<EventAttendant> eventAttendantList = new ArrayList<>();

    public Event(EventRequestDto eventRequestDto, long userId, long clubId) {
        this.ownerId = userId;
        this.clubId = clubId;
        this.eventTitle = eventRequestDto.getEventTitle();
        this.eventContent = eventRequestDto.getEventContent();
        this.eventLocation = eventRequestDto.getEventLocation();
        this.eventGroupSize = eventRequestDto.getEventGroupSize();
        this.eventStartTime = eventRequestDto.getEventStartTime();
//        super(); 를 쓸 수 있지 않을까?
    }

}