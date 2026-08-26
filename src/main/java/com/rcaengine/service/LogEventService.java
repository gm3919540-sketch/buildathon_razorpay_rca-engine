package com.rcaengine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rcaengine.dto.LogEventMessage;
import com.rcaengine.entity.Incident;
import com.rcaengine.entity.LogEvent;
import com.rcaengine.entity.Service;
import com.rcaengine.repository.LogEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class LogEventService {

    private final IncidentService incidentService;
    private final LogEventRepository logEventRepository;
    private final ServiceService serviceService;
    private final ExceptionFingerprintService fingerprintService;
    private final IncidentRCAProcessor incidentRCAProcessor;
    public LogEvent save(LogEventMessage message) throws JsonProcessingException {

        Service service = serviceService.findOrCreate(
                message.serviceName(),
                message.environment()
        );
        Incident incident = null;

        if ("ERROR".equalsIgnoreCase(message.level())) {

            boolean hadOpenIncident =
                    incidentService.hasOpenIncident(service);
            log.info(
                    "DEBUG: hadOpenIncident = {} for service = {}",
                    hadOpenIncident,
                    service.getName()
            );

            incident = incidentService.findOrCreateIncident(
                    service,
                    message
            );

            if (!hadOpenIncident) {
                log.info(
                        "DEBUG: Triggering automatic RCA for incident {}",
                        incident.getId()
                );
                incidentRCAProcessor.process(
                        incident.getId()
                );
            }
        }
        String fingerprint =
                fingerprintService.generateFingerprint(
                        message.exceptionType(),
                        message.message()
                );

        LogEvent logEvent = new LogEvent();

        logEvent.setService(service);
        logEvent.setIncident(incident);
        logEvent.setFingerprint(fingerprint);
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