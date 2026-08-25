package com.rcaengine.config;

import com.rcaengine.dto.LogEventMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, LogEventMessage> consumerFactory() {

        // JSON -> RCA Engine ke LogEventMessage mein convert karega
        JacksonJsonDeserializer<LogEventMessage> deserializer =
                new JacksonJsonDeserializer<>(LogEventMessage.class);

        // RCA Engine ka DTO trusted hai
        deserializer.addTrustedPackages("com.rcaengine.dto");

        Map<String, Object> properties = new HashMap<>();

        // Kafka broker
        properties.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092"
        );

        // Consumer group
        properties.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "rca-engine-group"
        );

        // Agar new group ho aur offset na ho,
        // to topic ke beginning se messages padhega
        properties.put(
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest"
        );

        // Kafka key ko String mein convert karega
        properties.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class
        );

        // Kafka value ko JSON se Java object mein convert karega
        properties.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JacksonJsonDeserializer.class
        );

        return new DefaultKafkaConsumerFactory<>(
                properties,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LogEventMessage>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, LogEventMessage>
                factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());

        return factory;
    }
}