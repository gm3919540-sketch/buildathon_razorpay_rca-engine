package com.rcaengine.dto;

public record GeneratedRCAResponse(
        Long id,
        Long incidentId,
        String rootCause,
        String evidence,
        String recommendedActions,
        String confidence,
        boolean reviewed,
        boolean indexed,
        String actualRootCause,
        String actualResolution
) {
}