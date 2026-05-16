package com.spendsmart.recurring.scheduler;



import com.spendsmart.recurring.service.RecurringService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecurringScheduler {

    private final RecurringService service;

    @Scheduled(cron = "0 0 0 * * ?") // midnight daily
    public void runDailyJob() {
        service.processUpcomingDue();
    }
}