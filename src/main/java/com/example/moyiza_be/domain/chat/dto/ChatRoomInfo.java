package com.example.moyiza_be.domain.chat.dto;

import com.example.moyiza_be.common.enums.ChatTypeEnum;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Data
public class ChatRoomInfo {
    private String chatThumbnail;
    private Long chatId;
    private ChatTypeEnum chatType;
    private Long roomIdentifier;
    private String roomName;
    private ChatMessageOutput lastMessage;
    private Long unreadMessage;

    @QueryProjection
    public ChatRoomInfo(Long chatId, ChatTypeEnum chatType, Long roomIdentifier, String roomName, String chatThumbnail) {
        this.chatId = chatId;
        this.chatType = chatType;
        this.roomIdentifier = roomIdentifier;
        this.roomName = roomName;
        this.chatThumbnail = chatThumbnail;
    }
}
