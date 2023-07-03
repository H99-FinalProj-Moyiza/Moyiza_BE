package com.example.moyiza_be.chat.entity;


import com.example.moyiza_be.common.utils.TimeStamped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class ChatRecord extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private Long senderId;
    private String content;

    public ChatRecord(Long chatId, Long senderId, String content) {
        this.chatId = chatId;
        this.senderId = senderId;
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public void setId(Long id){this.id = id;}
}
