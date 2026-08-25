package com.rcaengine.config;

import com.rcaengine.entity.HistoricalIncident;
import com.rcaengine.repository.HistoricalIncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class HistoricalIncidentSeeder implements CommandLineRunner {

    private final HistoricalIncidentRepository repository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) {
            return;
        }

        HistoricalIncident incident = new HistoricalIncident();

        incident.setTitle(
                "Redis connection pool exhaustion"
        );

        incident.setServiceName(
                "payment-service"
        );

        incident.setExceptionType(
                "RedisConnectionException"
        );

        incident.setSymptoms(
                "Payment requests failed with Redis connection errors. "
                        + "Error rate increased rapidly and payment latency increased."
        );

        incident.setRootCause(
                "Redis connections were not released correctly, "
                        + "causing the connection pool to become exhausted."
        );

        incident.setResolution(
                "Fixed Redis connection lifecycle management and "
                        + "added connection pool monitoring."
        );

        incident.setAffectedComponents(
                "PaymentService, RedisConnectionPool"
        );

        incident.setOccurredAt(
                LocalDateTime.now().minusDays(30)
        );

        repository.save(incident);
    }
}