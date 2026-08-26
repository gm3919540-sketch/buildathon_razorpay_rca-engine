package com.rcaengine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rcaengine.dto.RCAReport;
import com.rcaengine.entity.GeneratedRCA;
import com.rcaengine.entity.Incident;
import com.rcaengine.repository.GeneratedRCARepository;
import com.rcaengine.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.ai.document.Document;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RCAService {
    private final ObjectMapper objectMapper;
    private  final KnowledgeSearchService knowledgeSearchService;
    private  final IncidentRepository incidentRepository;
    private final ChatClient chatClient;
    private final GeneratedRCARepository generatedRCARepository;
    public RCAReport generateRCA(Long incidentId) throws JsonProcessingException {

        Incident incident = incidentRepository
                .findById(incidentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Incident not found: " + incidentId
                        )
                );

        String query = """
                Service: %s
                Incident: %s
                Description: %s
                """.formatted(
                incident.getService().getName(),
                incident.getTitle(),
                incident.getDescription()
        );

        List<Document> documents =
                knowledgeSearchService.search(query);

        String historicalEvidence =
                documents.stream()
                        .map(Document::getText)
                        .collect(Collectors.joining("\n\n---\n\n"));

        String prompt = """
                You are a production Root Cause Analysis assistant.
                
                CURRENT INCIDENT
                Service: %s
                Title: %s
                Severity: %s
                Description: %s
                
                HISTORICAL EVIDENCE
                %s
                
                Return ONLY valid JSON:
                
                {
                  "rootCause": "string",
                  "evidence": ["string"],
                  "recommendedActions": ["string"],
                  "confidence": "LOW"
                }
                
                Rules:
                - confidence must be LOW, MEDIUM, or HIGH.
                - Do not invent facts.
                - Distinguish evidence from inference.
                - If evidence is insufficient, explicitly say so.
                """.formatted(
                incident.getService().getName(),
                incident.getTitle(),
                incident.getSeverity(),
                incident.getDescription(),
                historicalEvidence
        );

        String response = chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();

        RCAReport report;
        try {
            report = objectMapper.readValue(
                    response,
                    RCAReport.class
            );
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException(
                    "Gemini returned invalid RCA JSON",
                    exception
            );
        }
        GeneratedRCA generatedRCA = new GeneratedRCA();

        generatedRCA.setIncident(incident);
        generatedRCA.setRootCause(report.rootCause());

        generatedRCA.setEvidence(
                String.join("\n", report.evidence())
        );

        generatedRCA.setRecommendedActions(
                String.join("\n", report.recommendedActions())
        );

        generatedRCA.setConfidence(
                report.confidence()
        );

        generatedRCARepository.save(generatedRCA);

        return report;

    }
    private String extractExceptionType(Incident incident) {
        return incident.getTitle();
    }
}