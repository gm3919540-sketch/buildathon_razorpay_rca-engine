package com.rcaengine.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rcaengine.dto.GeneratedRCAResponse;
import com.rcaengine.dto.RCAReport;
import com.rcaengine.dto.RCAReviewRequest;
import com.rcaengine.entity.GeneratedRCA;
import com.rcaengine.service.RCAService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rca")
@RequiredArgsConstructor
@Slf4j
public class RCAController {

    private final RCAService rcaService;

    @GetMapping("/incidents/{incidentId}")
    public RCAReport generateRCA(
            @PathVariable Long incidentId
    ) throws JsonProcessingException {

        return rcaService.generateRCA(incidentId);
    }

    @GetMapping("/incidents/{incidentId}/report")
    public GeneratedRCAResponse getExistingRCA(
            @PathVariable Long incidentId
    ) {
        return rcaService.getExistingRCA(incidentId);
    }

    @PutMapping("/incidents/{incidentId}/review")
    public GeneratedRCA reviewRCA(
            @PathVariable Long incidentId,
            @Valid @RequestBody RCAReviewRequest request
    ) {
        return rcaService.reviewRCA(
                incidentId,
                request
        );
    }

    @PostMapping("/incidents/{incidentId}/index")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void indexReviewedRCA(
            @PathVariable Long incidentId
    ) {
        rcaService.indexReviewedRCA(incidentId);
    }
}