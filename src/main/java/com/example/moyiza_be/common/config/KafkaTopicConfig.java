package com.example.moyiza_be.common.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

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
