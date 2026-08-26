package com.rcaengine.controller;

import com.rcaengine.dto.IncidentResponse;
import com.rcaengine.entity.Incident;
import com.rcaengine.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentRepository incidentRepository;

    @GetMapping
    public List<IncidentResponse> getAllIncidents() {

        return incidentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public IncidentResponse getIncident(
            @PathVariable Long id
    ) {

        Incident incident = incidentRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Incident not found: " + id
                        )
                );

        return toResponse(incident);
    }

    private IncidentResponse toResponse(
            Incident incident
    ) {

        return new IncidentResponse(
                incident.getId(),
                incident.getTitle(),
                incident.getDescription(),
                incident.getSeverity(),
                incident.getStatus(),
                incident.getService().getName(),
                incident.getStartedAt(),
                incident.getResolvedAt(),
                incident.getCreatedAt()
        );
    }
}