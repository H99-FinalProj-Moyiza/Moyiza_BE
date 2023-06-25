package com.example.moyiza_be.chat.kafka;


import com.example.moyiza_be.chat.dto.ChatMessageOutput;
import com.example.moyiza_be.common.redis.RedisCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaChatConsumer {
    private final SimpMessageSendingOperations sendingOperations;
    private final RedisCacheService cacheService;
    private final String CHAT_DESTINATION_PREFIX = "/chat/";
    private final String CHATALARM_DESTINATION_PREFIX = "/chatalarm/";
    @KafkaListener(topics = "chat", groupId = "chatServer0")
    public void consumeAndBroadcast(ChatMessageOutput chatMessage){
        Long chatId = chatMessage.getChatId();
        String destination = CHAT_DESTINATION_PREFIX + chatId;
        String alarmDestination = CHATALARM_DESTINATION_PREFIX + chatId;
        cacheService.addRecentChatToList(chatId.toString(), chatMessage);
        sendingOperations.convertAndSend(destination, chatMessage);
        sendingOperations.convertAndSend(alarmDestination, chatMessage);
        log.info("consumed chatMessage for destination : " + destination);
    }
}
