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

        String exceptionType = message.exceptionType();

        Optional<Incident> existingIncident =
                incidentRepository
                        .findFirstByServiceIdAndStatusAndTitleContainingIgnoreCaseOrderByStartedAtDesc(
                                service.getId(),
                                IncidentStatus.OPEN,
                                exceptionType
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
                service.getName()
                        + " - "
                        + message.exceptionType()
        );

        incident.setDescription(
                message.message()
        );

        incident.setSeverity(
                determineSeverity(message.level())
        );

        incident.setStatus(
                IncidentStatus.OPEN
        );

        incident.setService(
                service
        );

        incident.setStartedAt(
                message.timestamp()
        );

        return incidentRepository.save(
                incident
        );
    }

    private IncidentSeverity determineSeverity(
            String level
    ) {

        if (level == null) {
            return IncidentSeverity.LOW;
        }

        return switch (level.toUpperCase()) {

            case "ERROR" ->
                    IncidentSeverity.HIGH;

            case "WARN" ->
                    IncidentSeverity.MEDIUM;

            default ->
                    IncidentSeverity.LOW;
        };
    }
    public Incident resolveIncident(Long id) {

        Incident incident = incidentRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Incident not found: " + id
                        )
                );

        incident.setStatus(
                IncidentStatus.RESOLVED
        );

        incident.setResolvedAt(
                java.time.LocalDateTime.now()
        );

        return incidentRepository.save(
                incident
        );
    }

    public boolean hasOpenIncident(
            Service service
    ) {

        return incidentRepository
                .findByServiceId(
                        service.getId()
                )
                .stream()
                .anyMatch(
                        incident ->
                                incident.getStatus()
                                        == IncidentStatus.OPEN
                );
    }
}