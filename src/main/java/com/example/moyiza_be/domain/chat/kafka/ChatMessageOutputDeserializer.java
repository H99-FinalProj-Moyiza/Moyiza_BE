//package com.example.moyiza_be.chat.kafka;
//
//
//import com.example.moyiza_be.chat.dto.ChatMessageOutput;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.common.serialization.Deserializer;
//import org.springframework.kafka.support.serializer.DeserializationException;
//
//import java.nio.charset.StandardCharsets;
//import java.util.Map;
//
//@Slf4j
//public class ChatMessageOutputDeserializer implements Deserializer<ChatMessageOutput> {
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    @Override
//    public void configure(Map<String, ?> configs, boolean isKey) {
//        Deserializer.super.configure(configs, isKey);
//    }
//
//    @Override
//    public ChatMessageOutput deserialize(String topic, byte[] data) {
//        try {
//            if (data == null){
//                log.info("data is null while deserializing data from topic : " + topic);
//                return null;
//            }
//            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), ChatMessageOutput.class);
//        } catch (Exception e) {
//            throw new DeserializationException("Error deserializing byte[] to ChatMessageOutput",data,false, e.getCause());
//        }
//    }
//
//    @Override
//    public void close() {
//        Deserializer.super.close();
//    }
//}
