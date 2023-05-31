package com.example.moyiza_be.chat.dto;

import com.example.moyiza_be.chat.entity.ChatRecord;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Data
@NoArgsConstructor
public class ChatMessageOutput {
    private Long senderId;
    private String senderNickname;
    private String content;
    private LocalDateTime sentAt;
    private LocalDateTime modifiedAt;

    public ChatMessageOutput(ChatRecord chatRecord, String senderNickname) {
        this.senderId = chatRecord.getSenderId();
        this.senderNickname = senderNickname;
        this.content = chatRecord.getContent();
        this.sentAt = chatRecord.getCreatedAt();
        this.modifiedAt = chatRecord.getModifiedAt();
    }
}
