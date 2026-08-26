package com.rcaengine.service;

import com.rcaengine.dto.LogEventMessage;
import com.rcaengine.entity.Incident;
import com.rcaengine.entity.IncidentSeverity;
import com.rcaengine.entity.IncidentStatus;
import com.rcaengine.entity.Service;
import com.rcaengine.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public Incident findOrCreateIncident(
            Service service,
            LogEventMessage message
    ) {

        Optional<Incident> existingIncident =
                incidentRepository
                        .findFirstByServiceIdAndStatusOrderByStartedAtDesc(
                                service.getId(),
                                IncidentStatus.OPEN
                        );

        if (existingIncident.isPresent()) {
            return existingIncident.get();
        }

        return createIncident(service, message);
    }

    private Incident createIncident(
            Service service,
            LogEventMessage message
    ) {

        Incident incident = new Incident();

        incident.setTitle(
                service.getName() + " - " + message.exceptionType()
        );

        incident.setDescription(message.message());

        incident.setSeverity(
                determineSeverity(message.level())
        );

        incident.setStatus(IncidentStatus.OPEN);

        incident.setService(service);

        incident.setStartedAt(message.timestamp());

        return incidentRepository.save(incident);
    }

    private IncidentSeverity determineSeverity(String level) {

        return switch (level.toUpperCase()) {
            case "ERROR" -> IncidentSeverity.HIGH;
            case "WARN" -> IncidentSeverity.MEDIUM;
            default -> IncidentSeverity.LOW;
        };
    }
    public boolean hasOpenIncident(Service service) {

        return incidentRepository
                .findFirstByServiceIdAndStatusOrderByStartedAtDesc(
                        service.getId(),
                        IncidentStatus.OPEN
                )
                .isPresent();
    }
}