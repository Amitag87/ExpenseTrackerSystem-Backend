package com.spendsmart.analytics.controller;

import com.spendsmart.analytics.entity.FinancialSnapshot;
import com.spendsmart.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsResource {

    private final AnalyticsService service;

    // ================== CREATE ==================
    @PostMapping("/snapshot")
    public FinancialSnapshot createSnapshot(@RequestBody Map<String, Object> request) {

        Long userId = Long.valueOf(request.get("userId").toString());
        int year = (int) request.get("year");
        int month = (int) request.get("month");

        return service.generateMonthlySnapshot(userId, year, month);
    }

    // ================== READ ==================
    @GetMapping("/monthly/{userId}")
    public Map<String, Object> monthly(@PathVariable Long userId,
                                       @RequestParam int year,
                                       @RequestParam int month) {
        return service.getMonthlySummary(userId, year, month);
    }

    @GetMapping("/yearly/{userId}")
    public Map<String, Object> yearly(@PathVariable Long userId,
                                      @RequestParam int year) {
        return service.getYearlySummary(userId, year);
    }

    @GetMapping("/categories/{userId}")
    public Map<String, Double> categories(@PathVariable Long userId) {
        return service.getExpenseBreakdownByCategory(userId);
    }

    @GetMapping("/health/{userId}")
    public int health(@PathVariable Long userId) {
        return service.getFinancialHealthScore(userId);
    }

    // ================== UPDATE (FULL) ==================
    @PutMapping("/snapshot/{id}")
    public FinancialSnapshot updateSnapshot(@PathVariable Long id,
                                            @RequestBody FinancialSnapshot snapshot) {

        snapshot.setSnapshotId(id);
        return service.updateSnapshot(snapshot);
    }

    // ================== UPDATE (PARTIAL) ==================
    @PatchMapping("/snapshot/{id}")
    public FinancialSnapshot patchSnapshot(@PathVariable Long id,
                                           @RequestBody Map<String, Object> updates) {

        return service.patchSnapshot(id, updates);
    }

    // ================== DELETE ==================
    @DeleteMapping("/snapshot/{id}")
    public String deleteSnapshot(@PathVariable Long id) {

        service.deleteSnapshot(id);
        return "Deleted Successfully";
    }
}