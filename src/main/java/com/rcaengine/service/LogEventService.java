package com.rcaengine.service;

import com.rcaengine.dto.LogEventMessage;
import com.rcaengine.entity.Incident;
import com.rcaengine.entity.LogEvent;
import com.rcaengine.entity.Service;
import com.rcaengine.repository.LogEventRepository;
import lombok.RequiredArgsConstructor;


@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class LogEventService {
    private final IncidentService incidentService;
    private final LogEventRepository logEventRepository;
    private final ServiceService serviceService;

    public LogEvent save(LogEventMessage message) {

        Service service = serviceService.findOrCreate(
                message.serviceName(),
                message.environment()
        );
        Incident incident = null;
        if ("ERROR".equalsIgnoreCase(message.level())) {
            incident = incidentService.findOrCreateIncident(
                    service,
                    message
            );
        }

        LogEvent logEvent = new LogEvent();

        logEvent.setService(service);
        logEvent.setIncident(incident);
        logEvent.setTimestamp(message.timestamp());
        logEvent.setLevel(message.level());
        logEvent.setMessage(message.message());
        logEvent.setExceptionType(message.exceptionType());
        logEvent.setStackTrace(message.stackTrace());
        logEvent.setTraceId(message.traceId());
        logEvent.setEnvironment(message.environment());

        return logEventRepository.save(logEvent);
    }
}