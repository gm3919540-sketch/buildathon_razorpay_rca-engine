package com.rcaengine.service;

import com.rcaengine.dto.LogEventMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogEventConsumer {

    @KafkaListener(
            topics = "application-logs",
            groupId = "rca-engine-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(LogEventMessage event) {

        log.info("🔥 KAFKA MESSAGE RECEIVED");

        log.info(
                "Received log event from service: {}",
                event.serviceName()
        );

        log.info(
                "Message: {}",
                event.message()
        );

        log.info(
                "Trace ID: {}",
                event.traceId()
        );
    }
}