package com.rcaengine.service;

import com.rcaengine.dto.IncidentResponse;
import com.rcaengine.dto.LogEventMessage;
import com.rcaengine.entity.Incident;
import com.rcaengine.entity.IncidentSeverity;
import com.rcaengine.entity.IncidentStatus;
import com.rcaengine.entity.Service;
import com.rcaengine.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Optional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class IncidentService {

    private final SimpMessagingTemplate messagingTemplate;
    private final IncidentRepository incidentRepository;

    public IncidentResult findOrCreateIncident(
            Service service,
            LogEventMessage message
    ) {

        String exceptionType =
                message.exceptionType();

        Optional<Incident> existingIncident =
                incidentRepository
                        .findFirstByServiceIdAndStatusAndTitleContainingIgnoreCaseOrderByStartedAtDesc(
                                service.getId(),
                                IncidentStatus.OPEN,
                                exceptionType
                        );

        if (existingIncident.isPresent()) {

            return new IncidentResult(
                    existingIncident.get(),
                    false
            );
        }

        Incident incident =
                createIncident(
                        service,
                        message
                );

        return new IncidentResult(
                incident,
                true
        );
    }

    private Incident createIncident(
            Service service,
            LogEventMessage message
    ) {

        Incident incident =
                new Incident();

        incident.setTitle(
                service.getName()
                        + " - "
                        + message.exceptionType()
        );

        incident.setDescription(
                message.message()
        );

        incident.setSeverity(
                determineSeverity(
                        message.level()
                )
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

        Incident savedIncident =
                incidentRepository.save(
                        incident
                );

        IncidentResponse response =
                new IncidentResponse(
                        savedIncident.getId(),
                        savedIncident.getTitle(),
                        savedIncident.getDescription(),
                        savedIncident.getSeverity(),
                        savedIncident.getStatus(),
                        savedIncident.getService().getName(),
                        savedIncident.getStartedAt(),
                        savedIncident.getResolvedAt(),
                        savedIncident.getCreatedAt()
                );

        messagingTemplate.convertAndSend(
                "/topic/incidents",
                response
        );

        return savedIncident;
    }

    private IncidentSeverity determineSeverity(
            String level
    ) {

        if (level == null) {
            return IncidentSeverity.LOW;
        }

        return switch (
                level.toUpperCase()
                ) {

            case "ERROR" ->
                    IncidentSeverity.HIGH;

            case "WARN" ->
                    IncidentSeverity.MEDIUM;

            default ->
                    IncidentSeverity.LOW;
        };
    }

    public Incident resolveIncident(
            Long id
    ) {

        Incident incident =
                incidentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Incident not found: "
                                                + id
                                )
                        );

        incident.setStatus(
                IncidentStatus.RESOLVED
        );

        incident.setResolvedAt(
                java.time.LocalDateTime.now()
        );

        Incident savedIncident =
                incidentRepository.save(
                        incident
                );

        IncidentResponse response =
                new IncidentResponse(
                        savedIncident.getId(),
                        savedIncident.getTitle(),
                        savedIncident.getDescription(),
                        savedIncident.getSeverity(),
                        savedIncident.getStatus(),
                        savedIncident.getService().getName(),
                        savedIncident.getStartedAt(),
                        savedIncident.getResolvedAt(),
                        savedIncident.getCreatedAt()
                );

        messagingTemplate.convertAndSend(
                "/topic/incidents",
                response
        );

        return savedIncident;
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

    public record IncidentResult(
            Incident incident,
            boolean created
    ) {
    }
}