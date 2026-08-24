package com.rcaengine.repository;

import com.rcaengine.entity.LogEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogEventRepository extends JpaRepository<LogEvent, Long> {

    List<LogEvent> findByServiceId(Long serviceId);

    List<LogEvent> findByLevel(String level);
}