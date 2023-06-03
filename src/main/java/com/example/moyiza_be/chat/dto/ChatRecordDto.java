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
    private Long senderId;
    private String profileUrl;

    public ChatRecordDto(ChatRecord chatRecord) {
        this.id = chatRecord.getId();
        this.senderId = chatRecord.getSenderId();
        this.senderNickname = chatRecord.getSenderId().toString();
        this.profileUrl = "https://moyiza-image.s3.ap-northeast-2.amazonaws.com/20fa6d6a-09ba-4cf1-800b-6f225703c85c_shawn_raboutou2.jpg";
        this.content = chatRecord.getContent();
        this.sentAt = chatRecord.getCreatedAt();
        this.modifiedAt = chatRecord.getModifiedAt();
    }
}
