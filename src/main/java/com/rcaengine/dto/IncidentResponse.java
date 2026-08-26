package com.rcaengine.dto;

import com.rcaengine.entity.IncidentSeverity;
import com.rcaengine.entity.IncidentStatus;

import java.time.LocalDateTime;

public record IncidentResponse(
        Long id,
        String title,
        String description,
        IncidentSeverity severity,
        IncidentStatus status,
        String serviceName,
        LocalDateTime startedAt,
        LocalDateTime resolvedAt,
        LocalDateTime createdAt
) {
}