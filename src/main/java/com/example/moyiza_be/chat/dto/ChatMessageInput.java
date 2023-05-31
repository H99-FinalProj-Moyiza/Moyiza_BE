package com.example.moyiza_be.chat.dto;


import com.example.moyiza_be.chat.entity.ChatRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ChatMessageInput {

    private String content;
    private String senderNickname;
//
//    public enum MessageTypeEnum {
//        CHAT, JOIN, LEAVE
//    }

    public ChatMessageInput(String content, String senderNickname) {
        this.content = content;
        this.senderNickname = senderNickname;
    }

    public ChatRecord toChatRecord(Long chatId, Long userId){
        return new ChatRecord(chatId, userId, content);
    }

}
