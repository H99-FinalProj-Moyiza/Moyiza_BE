package com.example.moyiza_be.chat.dto;

import com.example.moyiza_be.chat.entity.Chat;
import com.example.moyiza_be.common.enums.ChatTypeEnum;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.Query;

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

    @QueryProjection
    public ChatRoomInfo(Long chatId, ChatTypeEnum chatType, Long roomIdentifier, String roomName, String chatThumbnail) {
        this.chatId = chatId;
        this.chatType = chatType;
        this.roomIdentifier = roomIdentifier;
        this.roomName = roomName;
        this.chatThumbnail = chatThumbnail;
    }
}
