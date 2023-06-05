package com.example.moyiza_be.chat.entity;

import com.example.moyiza_be.common.enums.ChatTypeEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private ChatTypeEnum chatType;
    private Long roomIdentifier; // club이나 oneday일 경우 clubId 혹은 onedayId
    private String roomName;

    public Chat(Long roomIdentifier, ChatTypeEnum chatType, String roomName) {
        this.chatType = chatType;
        this.roomIdentifier = roomIdentifier;
        this.roomName = roomName;
    }
}
