package com.rcaengine.controller;

import com.rcaengine.dto.HistoricalIncidentRequest;
import com.rcaengine.entity.HistoricalIncident;
import com.rcaengine.service.KnowledgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/knowledge")
@RequiredArgsConstructor
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    @PostMapping("/incidents")
    @ResponseStatus(HttpStatus.CREATED)
    public HistoricalIncident addHistoricalIncident(
            @Valid @RequestBody HistoricalIncidentRequest request
    ) {

        return knowledgeService.addHistoricalIncident(request);
    }
}