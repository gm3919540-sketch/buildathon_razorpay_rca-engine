package com.rcaengine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rcaengine.dto.RCAReport;
import com.rcaengine.dto.RCAReviewRequest;
import com.rcaengine.entity.GeneratedRCA;
import com.rcaengine.entity.Incident;
import com.rcaengine.repository.GeneratedRCARepository;
import com.rcaengine.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RCAService {
    private final VectorStore vectorStore;
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
    public GeneratedRCA reviewRCA(
            Long incidentId,
            RCAReviewRequest request
    ) {

        GeneratedRCA rca =
                generatedRCARepository
                        .findByIncidentId(incidentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "RCA not found for incident: "
                                                + incidentId
                                )
                        );

        rca.setReviewed(true);

        rca.setActualRootCause(
                request.actualRootCause()
        );

        rca.setActualResolution(
                request.actualResolution()
        );

        return generatedRCARepository.save(rca);
    }
    public void indexReviewedRCA(Long incidentId) {

        GeneratedRCA rca = generatedRCARepository
                .findByIncidentId(incidentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "RCA not found for incident: " + incidentId
                        )
                );

        if (!rca.isReviewed()) {
            throw new IllegalStateException(
                    "RCA must be reviewed before indexing"
            );
        }

        String content = """
            Verified Production RCA

            Service: %s

            AI Root Cause:
            %s

            Verified Root Cause:
            %s

            AI Evidence:
            %s

            Verified Resolution:
            %s

            AI Confidence:
            %s
            """.formatted(
                rca.getIncident().getService().getName(),
                rca.getRootCause(),
                rca.getActualRootCause(),
                rca.getEvidence(),
                rca.getActualResolution(),
                rca.getConfidence()
        );

        Map<String, Object> metadata = new HashMap<>();

        metadata.put(
                "sourceType",
                "VERIFIED_RCA"
        );

        metadata.put(
                "incidentId",
                incidentId
        );

        metadata.put(
                "serviceName",
                rca.getIncident()
                        .getService()
                        .getName()
        );

        metadata.put(
                "verified",
                true
        );

        Document document = new Document(
                content,
                metadata
        );

        vectorStore.add(
                List.of(document)
        );
    }
}