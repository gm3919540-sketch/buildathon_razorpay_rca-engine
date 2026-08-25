package com.rcaengine.repository;

import com.rcaengine.entity.HistoricalIncident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricalIncidentRepository
        extends JpaRepository<HistoricalIncident, Long> {

    List<HistoricalIncident> findByServiceName(
            String serviceName
    );

    List<HistoricalIncident> findByExceptionType(
            String exceptionType
    );
}