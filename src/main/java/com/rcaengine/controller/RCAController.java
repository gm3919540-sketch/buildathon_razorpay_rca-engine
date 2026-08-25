package com.rcaengine.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rcaengine.dto.RCAReport;
import com.rcaengine.service.RCAService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rca")
@RequiredArgsConstructor
@Slf4j
public class RCAController {

    private final RCAService rcaService;

@GetMapping("/incidents/{incidentId}")
public RCAReport generateRCA(
        @PathVariable Long incidentId
) throws JsonProcessingException {

    return rcaService.generateRCA(incidentId);
}
}