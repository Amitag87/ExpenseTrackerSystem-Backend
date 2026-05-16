package com.spendsmart.auth.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthResource {

    @GetMapping("/")
    public Map<String, String> root() {
        return Map.of(
                "service", "auth-service",
                "status", "running"
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
}
