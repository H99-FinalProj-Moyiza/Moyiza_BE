package com.example.moyiza_be.domain.chat.kafka;


import com.example.moyiza_be.domain.chat.dto.ChatMessageOutput;
import com.example.moyiza_be.common.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaChatProducer {
    private final KafkaTemplate<String, ChatMessageOutput> kafkaChatMessageTemplate;
    private final RedisService cacheService;
    private final String CHATTOPIC = "chat";

    public void publishMessage(ChatMessageOutput chatMessage){
        kafkaChatMessageTemplate.send("chat", chatMessage);
//        log.info("chatMessage published by " + chatMessage.getSenderId() + " chat Id : " + chatMessage.getChatId());
    }

}
