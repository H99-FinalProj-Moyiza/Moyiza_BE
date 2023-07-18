package com.example.moyiza_be.domain.chat.entity;

import com.example.moyiza_be.common.utils.TimeStamped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

//    private LocalDateTime lastDisconnected = LocalDateTime.now();
    private Long lastReadMessageId = 0L;
    public void setCurrentlyJoined(Boolean currentlyJoined) {
        isCurrentlyJoined = currentlyJoined;
    }

    public ChatJoinEntry(Long chatId, Long userId) {
        this.chatId = chatId;
        this.userId = userId;
        this.isCurrentlyJoined = true;
    }

    public void setLastReadMessageId(Long lastReadMessageId) {
        this.lastReadMessageId = lastReadMessageId;
    }
}
