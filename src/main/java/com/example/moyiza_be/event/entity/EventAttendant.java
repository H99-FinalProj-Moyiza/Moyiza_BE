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
    private Long id;
    @Column
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "eventId")
    private Long eventId;
    @Column
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userId")
    private Long userId;
    private String userName;
    private String userNickName;
    private String userEmail;
    private String userImage;


//    @Column
//    private boolean entrance;

    public EventAttendant(Long EventId, Long userId, User user) {
        this.eventId = EventId;
        this.userId = userId;
        this.userName = user.getName();
        this.userNickName = user.getNickname();
        this.userEmail = user.getEmail();
        this.userImage = user.getProfileImage();
    }

//    public void cancelAttendant(Event event) {
//        this.event = event;
//        this.entrance = false;
//    }
}
