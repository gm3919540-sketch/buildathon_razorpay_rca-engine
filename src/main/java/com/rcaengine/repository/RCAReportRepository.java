package com.rcaengine.repository;

import com.rcaengine.entity.RCAReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RCAReportRepository extends JpaRepository<RCAReport, Long> {

    Optional<RCAReport> findByIncidentId(Long incidentId);
}