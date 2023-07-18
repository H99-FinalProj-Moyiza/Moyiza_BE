//package com.example.moyiza_be.chat.kafka;
//
//
//import com.example.moyiza_be.chat.dto.ChatMessageOutput;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.common.errors.SerializationException;
//import org.apache.kafka.common.serialization.Serializer;
//
//import java.util.Map;
//
//@Slf4j
//public class ChatMessageOutputSerializer implements Serializer<ChatMessageOutput> {
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    @Override
//    public byte[] serialize(String topic, ChatMessageOutput chatMessage) {
//        try {
//            if (chatMessage == null){
//                log.info("Null received at serializing ChatMessageOutput for topic : " + topic);
//                return null;
//            }
//            return objectMapper.writeValueAsBytes(chatMessage);
//        } catch (Exception e) {
//            throw new SerializationException("Error when serializing MessageDto to byte[]");
//        }
//    }
//
//}
