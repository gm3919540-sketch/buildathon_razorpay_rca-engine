package com.rcaengine.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record HistoricalIncidentRequest(

        @NotBlank
        String title,

        @NotBlank
        String serviceName,

        @NotBlank
        String exceptionType,

        @NotBlank
        String symptoms,

        @NotBlank
        String rootCause,

        String resolution,

        String affectedComponents,

        LocalDateTime occurredAt
) {
}