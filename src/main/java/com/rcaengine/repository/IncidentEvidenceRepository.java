package com.rcaengine.repository;

import com.rcaengine.entity.IncidentEvidence;
import com.rcaengine.entity.EvidenceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentEvidenceRepository
        extends JpaRepository<IncidentEvidence, Long> {

    List<IncidentEvidence> findByIncidentId(Long incidentId);

    List<IncidentEvidence> findByIncidentIdAndType(
            Long incidentId,
            EvidenceType type
    );
}