package com.rcaengine.service;

import com.rcaengine.dto.HistoricalIncidentRequest;
import com.rcaengine.entity.HistoricalIncident;
import com.rcaengine.repository.HistoricalIncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KnowledgeService {

    private final HistoricalIncidentRepository repository;

    public HistoricalIncident addHistoricalIncident(
            HistoricalIncidentRequest request
    ) {

        HistoricalIncident incident =
                new HistoricalIncident();

        incident.setTitle(request.title());
        incident.setServiceName(request.serviceName());
        incident.setExceptionType(request.exceptionType());
        incident.setSymptoms(request.symptoms());
        incident.setRootCause(request.rootCause());
        incident.setResolution(request.resolution());
        incident.setAffectedComponents(
                request.affectedComponents()
        );

        incident.setOccurredAt(
                request.occurredAt() != null
                        ? request.occurredAt()
                        : LocalDateTime.now()
        );

        return repository.save(incident);
    }
}