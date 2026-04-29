package com.spendsmart.analytics.service;

import com.spendsmart.analytics.entity.FinancialSnapshot;

import java.util.*;

public interface AnalyticsService {

    // ================== CREATE ==================
    FinancialSnapshot generateMonthlySnapshot(Long userId, int year, int month);

    // ================== READ ==================
    Map<String, Object> getMonthlySummary(Long userId, int year, int month);

    Map<String, Object> getYearlySummary(Long userId, int year);

    Map<String, Double> getExpenseBreakdownByCategory(Long userId);

    List<Map<String, Object>> getIncomeVsExpenseTrend(Long userId);

    List<Double> getSavingsRateTrend(Long userId);

    List<String> getTopSpendingCategories(Long userId);

    List<Double> getDailyExpenseTrend(Long userId);

    Map<String, Double> getCashflowData(Long userId);

    double getSpendingForecast(Long userId);

    int getFinancialHealthScore(Long userId);

    // ================== UPDATE ==================
    FinancialSnapshot updateSnapshot(FinancialSnapshot snapshot);

    FinancialSnapshot patchSnapshot(Long id, Map<String, Object> updates);

    // ================== DELETE ==================
    void deleteSnapshot(Long id);
}