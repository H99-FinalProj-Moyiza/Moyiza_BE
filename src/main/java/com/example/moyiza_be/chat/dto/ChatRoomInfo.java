package com.example.moyiza_be.chat.dto;

import com.example.moyiza_be.chat.entity.Chat;
import com.example.moyiza_be.common.enums.ChatTypeEnum;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Data
public class ChatRoomInfo {
    private Long chatId;
    private ChatTypeEnum chatType;
    private Long roomIdentifier;

    public ChatRoomInfo(Chat chat) {
        this.chatId = chat.getId();
        this.chatType = chat.getChatType();
        this.roomIdentifier = chat.getRoomIdentifier();
    }

}
