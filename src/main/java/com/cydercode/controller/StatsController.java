package com.cydercode.controller;

import com.cydercode.dto.stats.StatsResponse;
import com.cydercode.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/stats")
    public StatsResponse getStats() {
        return statsService.getStats();
    }
}
