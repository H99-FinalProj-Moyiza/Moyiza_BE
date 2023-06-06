package com.example.moyiza_be.chat.dto;

import com.example.moyiza_be.chat.entity.ChatRecord;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@NoArgsConstructor
public class ChatMessageOutput {
    private Long chatRecordId;
    private Long senderId;
    private String senderNickname;
    private String senderProfileUrl;
    private String content;
    private String sentAt;
    private String modifiedAt;
    private Long unreadCount = 0L;


    public ChatMessageOutput(ChatRecord chatRecord, ChatUserPrincipal userPrincipal, Long unreadCount) {
        this.senderId = chatRecord.getSenderId();
        this.senderProfileUrl = userPrincipal.getProfileUrl();
        this.senderNickname = userPrincipal.getUserNickname();
        this.chatRecordId = chatRecord.getId();
        this.content = chatRecord.getContent();
        this.sentAt = chatRecord.getCreatedAt().toString();
        this.modifiedAt = chatRecord.getModifiedAt().toString();
        this.unreadCount = unreadCount;
    }

    public ChatMessageOutput(ChatRecord chatRecord, Long memberCount, Long readCount){
        this.senderId = chatRecord.getSenderId();
        this.senderProfileUrl = "https://moyiza-image.s3.ap-northeast-2.amazonaws.com/20fa6d6a-09ba-4cf1-800b-6f225703c85c_shawn_raboutou2.jpg";
        this.senderNickname = chatRecord.getSenderId().toString();
        this.chatRecordId = chatRecord.getId();
        this.content = chatRecord.getContent();
        this.sentAt = chatRecord.getCreatedAt().toString();
        this.modifiedAt = chatRecord.getModifiedAt().toString();
        this.unreadCount = memberCount - readCount;
    }

    public void setUnreadCount(Long unreadCount) {
        this.unreadCount = unreadCount;
    }
}
