package com.rcaengine.controller;

import com.rcaengine.dto.HistoricalIncidentRequest;
import com.rcaengine.entity.HistoricalIncident;
import com.rcaengine.service.KnowledgeSearchService;
import com.rcaengine.service.KnowledgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
@RequiredArgsConstructor
public class KnowledgeController {

    private final KnowledgeSearchService knowledgeSearchService;
    private final KnowledgeService knowledgeService;

    @PostMapping("/incidents")
    @ResponseStatus(HttpStatus.CREATED)
    public HistoricalIncident addHistoricalIncident(
            @Valid @RequestBody HistoricalIncidentRequest request
    ) {

        return knowledgeService.addHistoricalIncident(request);
    }
    @GetMapping("/search")
    public List<Document> search(
            @RequestParam String query
    ) {
        return knowledgeSearchService.search(query);
    }
}