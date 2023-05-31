package com.example.moyiza_be.event.entity;

import com.example.moyiza_be.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class EventAttendant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column
    private boolean entrance;

    public EventAttendant(Event event, User user) {
        this.event = event;
        this.user = user;
        this.entrance = false;
    }

    public void cancelAttendant(Event event) {
        this.event = event;
        this.entrance = false;
    }
}
