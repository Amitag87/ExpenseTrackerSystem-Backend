package com.spendsmart.analytics.scheduler;

import com.spendsmart.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SnapshotScheduler {

    private final AnalyticsService service;

    @Scheduled(cron = "0 0 1 1 * ?")
    public void generateSnapshots() {
        Long userId = 1L; // loop all users later

        LocalDate now = LocalDate.now();
        service.generateMonthlySnapshot(userId, now.getYear(), now.getMonthValue());
    }
}