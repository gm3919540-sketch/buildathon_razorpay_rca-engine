package com.rcaengine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rcaengine.entity.Incident;
import com.rcaengine.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentRCAProcessor {

    private final IncidentRepository incidentRepository;
    private final RCAService rcaService;
    @Transactional
    public void process(Long incidentId) throws JsonProcessingException {

        log.info("Starting automated RCA for incident: {}", incidentId);

        Incident incident = incidentRepository
                .findById(incidentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Incident not found: " + incidentId
                        )
                );

        log.info(
                "Incident found: {} - {}",
                incident.getId(),
                incident.getTitle()
        );

        log.info("Calling RCAService.generateRCA() for incident: {}", incidentId);

        try {

            rcaService.generateRCA(incidentId);

            log.info(
                    "Automated RCA completed for incident: {}",
                    incidentId
            );

        } catch (Exception e) {

            log.error(
                    "❌ AUTOMATED RCA FAILED for incident: {}",
                    incidentId,
                    e
            );

            throw e;
        }
    }
}