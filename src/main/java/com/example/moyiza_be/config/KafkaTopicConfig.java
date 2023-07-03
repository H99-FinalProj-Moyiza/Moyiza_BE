package com.example.moyiza_be.config;

import com.example.moyiza_be.chat.dto.ChatMessageOutput;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaTopicConfig {
//    private final KafkaProperties kafkaProperties;
//    public Map<String, Object> chatProducerConfigs() {
//        Map<String, Object> props =
//                new HashMap<>(kafkaProperties.buildProducerProperties());
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
//                StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
//                JsonSerializer.class);
//        return props;
//    }
//    @Bean
//    public ProducerFactory<String, ChatMessageOutput> producerFactory() {
//        return new DefaultKafkaProducerFactory<>(chatProducerConfigs());
//    }
//
//    @Bean
//    public KafkaTemplate<String, ChatMessageOutput> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }
    @Bean
    public NewTopic moyizaChatTopicBuilder(){
        return TopicBuilder.name("chat")
//                .partitions(1)
                .build();
    }
}
