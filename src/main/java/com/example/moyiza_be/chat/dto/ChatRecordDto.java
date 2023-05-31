package com.example.moyiza_be.chat.dto;

import com.example.moyiza_be.chat.entity.ChatRecord;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Data
public class ChatRecordDto {
    private Long id;
    private String senderNickname;
    private String content;
    private LocalDateTime sentAt;
    private LocalDateTime modifiedAt;

    public ChatRecordDto(ChatRecord chatRecord) {
        this.id = chatRecord.getId();
        this.senderNickname = chatRecord.getSenderId().toString();
        this.content = chatRecord.getContent();
        this.sentAt = chatRecord.getCreatedAt();
        this.modifiedAt = chatRecord.getModifiedAt();
    }
}
