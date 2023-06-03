package com.example.moyiza_be.chat.entity;

import com.example.moyiza_be.common.utils.TimeStamped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class ChatJoinEntry extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;

    private Long userId;

    private Boolean isCurrentlyJoined;

    private LocalDateTime lastDisconnected = LocalDateTime.now();
    public void setCurrentlyJoined(Boolean currentlyJoined) {
        isCurrentlyJoined = currentlyJoined;
    }

    public ChatJoinEntry(Long chatId, Long userId) {
        this.chatId = chatId;
        this.userId = userId;
        this.isCurrentlyJoined = true;
    }

    public void setLastDisconnected(LocalDateTime lastDisconnected) {
        this.lastDisconnected = lastDisconnected;
    }
}
