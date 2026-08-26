package com.rcaengine.dto;

import jakarta.validation.constraints.NotBlank;

public record RCAReviewRequest(

        Boolean approved,

        @NotBlank
        String actualRootCause,

        @NotBlank
        String actualResolution
) {
}