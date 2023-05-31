package com.example.moyiza_be.chat.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ChatMessageDto {

    private String content;
    private String senderNickname;
    private MessageTypeEnum messageTypeEnum;

    public enum MessageTypeEnum {
        CHAT, JOIN, LEAVE
    }




}
