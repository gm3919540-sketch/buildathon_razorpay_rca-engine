package com.rcaengine.repository;

import com.rcaengine.entity.Incident;
import com.rcaengine.entity.IncidentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {

    List<Incident> findByStatus(IncidentStatus status);

    List<Incident> findByServiceId(Long serviceId);
}