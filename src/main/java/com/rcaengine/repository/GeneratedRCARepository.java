package com.rcaengine.repository;

import com.rcaengine.entity.GeneratedRCA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GeneratedRCARepository
        extends JpaRepository<GeneratedRCA, Long> {

    Optional<GeneratedRCA> findByIncidentId(Long incidentId);
}