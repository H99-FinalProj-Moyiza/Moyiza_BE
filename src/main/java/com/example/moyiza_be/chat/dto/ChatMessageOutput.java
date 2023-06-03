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
    private Long chatRecordId;
    private Long senderId;
    private String senderNickname;
    private String senderProfileUrl;
    private String content;
    private LocalDateTime sentAt;
    private LocalDateTime modifiedAt;


    public ChatMessageOutput(ChatRecord chatRecord, ChatUserPrincipal userPrincipal) {
        this.senderId = chatRecord.getSenderId();
        this.senderProfileUrl = userPrincipal.getProfileUrl();
        this.senderNickname = userPrincipal.getUserNickname();
        this.chatRecordId = chatRecord.getId();
        this.content = chatRecord.getContent();
        this.sentAt = chatRecord.getCreatedAt();
        this.modifiedAt = chatRecord.getModifiedAt();
    }
}
