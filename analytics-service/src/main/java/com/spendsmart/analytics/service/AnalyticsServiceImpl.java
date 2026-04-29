package com.spendsmart.analytics.service;

import com.spendsmart.analytics.entity.FinancialSnapshot;
import com.spendsmart.analytics.repository.AnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AnalyticsRepository repo;

    // ================== CREATE ==================
    @Override
    public FinancialSnapshot generateMonthlySnapshot(Long userId, int year, int month) {

        double income = 50000; // TODO: integrate service
        double expense = 30000;

        double savings = income - expense;
        double savingsRate = (income == 0) ? 0 : (savings / income) * 100;

        FinancialSnapshot snapshot = new FinancialSnapshot(
                null, userId, "MONTHLY", year, month,
                income, expense, savings, savingsRate,
                "Food", LocalDateTime.now()
        );

        return repo.save(snapshot);
    }

    // ================== READ ==================
    @Override
    public Map<String, Object> getMonthlySummary(Long userId, int year, int month) {
        FinancialSnapshot f = repo.findByUserIdAndYearAndMonth(userId, year, month)
                .orElseThrow(() -> new RuntimeException("Snapshot not found"));

        return Map.of(
                "income", f.getTotalIncome(),
                "expense", f.getTotalExpenses(),
                "savings", f.getNetSavings()
        );
    }

    @Override
    public Map<String, Object> getYearlySummary(Long userId, int year) {
        List<FinancialSnapshot> list = repo.findByUserIdAndYear(userId, year);

        double income = list.stream().mapToDouble(FinancialSnapshot::getTotalIncome).sum();
        double expense = list.stream().mapToDouble(FinancialSnapshot::getTotalExpenses).sum();

        return Map.of("income", income, "expense", expense);
    }

    @Override
    public Map<String, Double> getExpenseBreakdownByCategory(Long userId) {
        Map<String, Double> map = new HashMap<>();

        repo.sumExpensesByCategory(userId).forEach(o -> {
            map.put((String) o[0], (Double) o[1]);
        });

        return map;
    }

    @Override
    public List<Map<String, Object>> getIncomeVsExpenseTrend(Long userId) {
        List<FinancialSnapshot> list = repo.findByUserId(userId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (FinancialSnapshot f : list) {
            result.add(Map.of(
                    "month", f.getMonth(),
                    "income", f.getTotalIncome(),
                    "expense", f.getTotalExpenses()
            ));
        }
        return result;
    }

    @Override
    public List<Double> getSavingsRateTrend(Long userId) {
        return repo.findByUserId(userId)
                .stream()
                .map(FinancialSnapshot::getSavingsRate)
                .toList();
    }

    @Override
    public List<String> getTopSpendingCategories(Long userId) {
        return repo.findTopSpendingMonths(userId)
                .stream()
                .map(FinancialSnapshot::getTopCategory)
                .toList();
    }

    @Override
    public List<Double> getDailyExpenseTrend(Long userId) {
        return List.of(200.0, 500.0, 300.0); // mock
    }

    @Override
    public Map<String, Double> getCashflowData(Long userId) {
        return Map.of(
                "inflow", 50000.0,
                "outflow", 30000.0
        );
    }

    @Override
    public double getSpendingForecast(Long userId) {
        List<FinancialSnapshot> list = repo.findTopSpendingMonths(userId);

        return list.stream()
                .limit(3)
                .mapToDouble(FinancialSnapshot::getTotalExpenses)
                .average()
                .orElse(0);
    }

    @Override
    public int getFinancialHealthScore(Long userId) {
        Double savingsRate = repo.avgSavingsRateByUserId(userId);

        if (savingsRate == null) savingsRate = 0.0;

        double budgetScore = 80;
        double expenseRatio = 60;

        return (int) ((savingsRate * 0.4) +
                      (budgetScore * 0.4) +
                      (expenseRatio * 0.2));
    }

    // ================== UPDATE (PUT) ==================
    @Override
    public FinancialSnapshot updateSnapshot(FinancialSnapshot snapshot) {

        if (!repo.existsById(snapshot.getSnapshotId())) {
            throw new RuntimeException("Snapshot not found");
        }

        return repo.save(snapshot);
    }

    // ================== PATCH ==================
    @Override
    public FinancialSnapshot patchSnapshot(Long id, Map<String, Object> updates) {

        FinancialSnapshot snapshot = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Snapshot not found"));

        if (updates.containsKey("totalIncome")) {
            snapshot.setTotalIncome(Double.valueOf(updates.get("totalIncome").toString()));
        }

        if (updates.containsKey("totalExpenses")) {
            snapshot.setTotalExpenses(Double.valueOf(updates.get("totalExpenses").toString()));
        }

        if (updates.containsKey("topCategory")) {
            snapshot.setTopCategory(updates.get("topCategory").toString());
        }

        return repo.save(snapshot);
    }

    // ================== DELETE ==================
    @Override
    public void deleteSnapshot(Long id) {

        if (!repo.existsById(id)) {
            throw new RuntimeException("Snapshot not found");
        }

        repo.deleteById(id);
    }
}