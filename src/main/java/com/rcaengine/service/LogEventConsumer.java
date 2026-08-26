package com.rcaengine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rcaengine.dto.LogEventMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogEventConsumer {
    private final LogEventService logEventService;

    public LogEventConsumer(LogEventService logEventService) {
        this.logEventService = logEventService;
    }

    @KafkaListener(
            topics = "application-logs",
            groupId = "rca-engine-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(LogEventMessage event) throws JsonProcessingException {

        log.info("🔥 KAFKA MESSAGE RECEIVED");

        log.info(
                "Received log event from service: {}",
                event.serviceName()
        );
        logEventService.save(event);

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