package com.rcaengine.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public String health() {
        return "RCA Engine is running";
    }
    @GetMapping("/api/test-error")
    public String testError() {
        throw new RuntimeException("This is a test exception");
    }
}