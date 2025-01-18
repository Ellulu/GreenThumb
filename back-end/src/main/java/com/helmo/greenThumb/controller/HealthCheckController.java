package com.helmo.greenThumb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/health")
public class HealthCheckController {
    @GetMapping
    public ResponseEntity<Void> helloWorld() {
        return ResponseEntity.ok().build();
    }
}
