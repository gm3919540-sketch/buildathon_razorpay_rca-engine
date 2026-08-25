package com.rcaengine.dto;

import java.util.List;

public record RCAReport(
        String rootCause,
        List<String> evidence,
        List<String> recommendedActions,
        String confidence
) {
}