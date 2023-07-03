package com.example.moyiza_be.alert.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String message;

    @Column
    private String receiver;

    @Column
    private String sender;

    @Column
    private Long oneDayId;
    @Column
    private String oneDayTitle;
    @Column
    private String imageUrl;


    @Column
    private boolean checking;

    // If Sender Change NickName
    public void updateAlert(String sender) { // String imgUrl needed if image keep go with.
        this.sender = sender;
        this.message = sender + " is try to join!";
        this.imageUrl = imageUrl;
//        this.oneDayId = oneDayId;
//        this.oneDayTitle = oneDayTitle;
    }
}
