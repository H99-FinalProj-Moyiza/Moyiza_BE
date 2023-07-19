package com.example.moyiza_be.domain.chat.utils;

import com.example.moyiza_be.common.enums.MessageDestinationEnum;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class StompUtils {

    public MessageDestinationEnum destinationChecker(String destination) {
        if(destination == null){
            return null;
        }
        String type = destination.split("/")[1];
        if (type.equals("chat")) {
            return MessageDestinationEnum.CHAT;
        }
        if (type.equals("chatalarm")) {
            return MessageDestinationEnum.CHATALARM;
        }
        else{
            throw new IllegalArgumentException("unknown destination type");
        }
    }

    public Long getChatIdFromDestination(String destination) {
        if (destination == null) {
            throw new NullPointerException("subscribe destination not defined");
        }
        return Long.valueOf(destination.replaceAll("\\D", ""));
    }

    //When a user SUBSCRIBES, this message tells the client the ID of the last message read by that user.
    public Message<byte[]> buildLastReadMessage(String destination, Long lastReadMessageId, String userId) {
        StompHeaderAccessor newHeader = StompHeaderAccessor.create(StompCommand.MESSAGE);
        newHeader.setDestination(destination);
        newHeader.setNativeHeader("lastReadMessage", String.valueOf(lastReadMessageId));
        newHeader.setNativeHeader("subscribedUserId", userId);
        return MessageBuilder.createMessage("new subscription".getBytes(StandardCharsets.UTF_8), newHeader.getMessageHeaders());
    }

    public void sendLastReadMessage(String destination, Long lastReadMessageId, String userId, MessageChannel channel){
        Message<byte[]> message = buildLastReadMessage(destination, lastReadMessageId, userId);
        channel.send(message);
    }

}
