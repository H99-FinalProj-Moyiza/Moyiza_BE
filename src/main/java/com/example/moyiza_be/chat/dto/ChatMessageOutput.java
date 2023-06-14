package com.example.moyiza_be.chat.dto;

import com.example.moyiza_be.chat.entity.ChatRecord;
import com.querydsl.core.annotations.QueryInit;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Data
@NoArgsConstructor
public class ChatMessageOutput {
    private Long chatId;
    private Long chatRecordId;
    private Long senderId;
    private String senderNickname;
    private String senderProfileUrl;
    private String content;
    private String sentAt;
    private String modifiedAt;
    private Long unreadCount = 0L;


    public ChatMessageOutput(ChatRecord chatRecord, ChatUserPrincipal userPrincipal, Long unreadCount) {
        this.chatId = chatRecord.getChatId();
        this.senderId = chatRecord.getSenderId();
        this.senderProfileUrl = userPrincipal.getProfileUrl();
        this.senderNickname = userPrincipal.getUserNickname();
        this.chatRecordId = chatRecord.getId();
        this.content = chatRecord.getContent();
        this.sentAt = chatRecord.getCreatedAt().toString();
        this.modifiedAt = chatRecord.getModifiedAt().toString();
        this.unreadCount = unreadCount;
    }

    @QueryProjection
    public ChatMessageOutput(Long chatId, Long chatRecordId, Long senderId, String senderNickname,
                             String senderProfileUrl, String content, LocalDateTime sentAt, LocalDateTime modifiedAt) {
        this.chatId = chatId;
        this.chatRecordId = chatRecordId;
        this.senderId = senderId;
        this.senderNickname = senderNickname;
        this.senderProfileUrl = senderProfileUrl;
        this.content = content;
        this.sentAt = sentAt.toString();
        this.modifiedAt = modifiedAt.toString();
    }

    public void setUnreadCount(Long unreadCount) {
        this.unreadCount = unreadCount;
    }
}
