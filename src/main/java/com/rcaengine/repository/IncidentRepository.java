package com.rcaengine.repository;

import com.rcaengine.entity.Incident;
import com.rcaengine.entity.IncidentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IncidentRepository extends JpaRepository<Incident, Long> {

    List<Incident> findByStatus(IncidentStatus status);

    List<Incident> findByServiceId(Long serviceId);
    Optional<Incident> findFirstByServiceIdAndStatusOrderByStartedAtDesc(
            Long serviceId,
            IncidentStatus status
    );
}