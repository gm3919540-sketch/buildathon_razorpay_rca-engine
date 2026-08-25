package com.rcaengine.service;

import com.rcaengine.entity.Service;
import com.rcaengine.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public Service findOrCreate(
            String serviceName,
            String environment
    ) {
        return serviceRepository
                .findByName(serviceName)
                .orElseGet(() -> {
                    Service service = new Service();
                    service.setName(serviceName);
                    service.setDescription(
                            "Automatically discovered service"
                    );
                    service.setEnvironment(environment);

                    return serviceRepository.save(service);
                });
    }
}