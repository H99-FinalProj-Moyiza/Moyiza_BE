package com.example.moyiza_be.domain.chat.dto;


import com.example.moyiza_be.domain.chat.entity.ChatRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ChatMessageInput {

    private String content;
//
//    public enum MessageTypeEnum {
//        CHAT, JOIN, LEAVE
//    }

    public ChatMessageInput(String content) {
        this.content = content;
    }

    public ChatRecord toChatRecord(Long chatId, Long userId){
        return new ChatRecord(chatId, userId, content);
    }

}
