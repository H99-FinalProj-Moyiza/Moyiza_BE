package com.example.moyiza_be.chat.kafka;


import com.example.moyiza_be.chat.dto.ChatMessageOutput;
import com.example.moyiza_be.common.redis.RedisCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaChatConsumer {
    private final SimpMessageSendingOperations sendingOperations;
    private final String CHAT_DESTINATION_PREFIX = "/chat/";
    private final String CHATALARM_DESTINATION_PREFIX = "/chatalarm/";

    @KafkaListener(topics = "chat")
    public void consumeAndBroadcast(ChatMessageOutput chatMessage){
        Long chatId = chatMessage.getChatId();
        String destination = CHAT_DESTINATION_PREFIX + chatId;
        String alarmDestination = CHATALARM_DESTINATION_PREFIX + chatId;
        sendingOperations.convertAndSend(destination, chatMessage);
        sendingOperations.convertAndSend(alarmDestination, chatMessage);
    }
}
