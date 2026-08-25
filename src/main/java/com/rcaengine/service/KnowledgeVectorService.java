package com.rcaengine.service;

import com.rcaengine.entity.HistoricalIncident;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KnowledgeVectorService {

    private final VectorStore vectorStore;

    public void indexHistoricalIncident(
            HistoricalIncident incident
    ) {

        String content = buildContent(incident);

        Map<String, Object> metadata = new HashMap<>();

        metadata.put(
                "sourceType",
                "HISTORICAL_INCIDENT"
        );

        metadata.put(
                "historicalIncidentId",
                incident.getId()
        );

        metadata.put(
                "serviceName",
                incident.getServiceName()
        );

        metadata.put(
                "exceptionType",
                incident.getExceptionType()
        );

        Document document = new Document(
                content,
                metadata
        );

        vectorStore.add(
                java.util.List.of(document)
        );
    }

    private String buildContent(
            HistoricalIncident incident
    ) {

        return """
                Title: %s
                Service: %s
                Exception Type: %s
                Symptoms: %s
                Root Cause: %s
                Resolution: %s
                Affected Components: %s
                """.formatted(
                incident.getTitle(),
                incident.getServiceName(),
                incident.getExceptionType(),
                incident.getSymptoms(),
                incident.getRootCause(),
                incident.getResolution(),
                incident.getAffectedComponents()
        );
    }
}