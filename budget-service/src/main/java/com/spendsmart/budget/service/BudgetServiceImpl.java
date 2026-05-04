package com.spendsmart.budget.service;


import com.spendsmart.budget.client.NotificationClient;
import com.spendsmart.budget.dto.BudgetProgress;
import com.spendsmart.budget.entity.Budget;
import com.spendsmart.budget.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepo;
    private final NotificationClient notificationClient;

    @Override
    public Budget createBudget(Budget budget) {
        validateCategorySelection(budget);
        budget.setSpentAmount(0.0);
        budget.setActive(true);
        if (budget.getStartDate() == null) {
            budget.setStartDate(LocalDate.now());
        }
        return budgetRepo.save(budget);
    }

    @Override
    public Optional<Budget> getBudgetById(Long budgetId) {
        return budgetRepo.findByBudgetId(budgetId);
    }

    @Override
    public List<Budget> getBudgetsByUser(Long userId) {
        return budgetRepo.findByUserId(userId);
    }

    @Override
    public List<Budget> getActiveBudgets(Long userId) {
        return budgetRepo.findByUserIdAndIsActive(userId, true);
    }

    @Override
    public Budget updateBudget(Long budgetId, Budget updatedBudget) {
        validateCategorySelection(updatedBudget);
        Budget existing = budgetRepo.findByBudgetId(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found with id: " + budgetId));

        existing.setName(updatedBudget.getName());
        existing.setLimitAmount(updatedBudget.getLimitAmount());
        existing.setCurrency(updatedBudget.getCurrency());
        existing.setPeriod(updatedBudget.getPeriod());
        existing.setStartDate(updatedBudget.getStartDate());
        existing.setEndDate(updatedBudget.getEndDate());
        existing.setAlertThreshold(updatedBudget.getAlertThreshold());
        existing.setActive(updatedBudget.isActive());
        existing.setCategoryId(updatedBudget.getCategoryId());

        return budgetRepo.save(existing);
    }

    @Override
    @Transactional
    public void deleteBudget(Long budgetId) {
        budgetRepo.deleteByBudgetId(budgetId);
    }

    @Override
    public void updateSpentAmount(Long budgetId, double amount) {
        Budget budget = budgetRepo.findByBudgetId(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found with id: " + budgetId));
        double previousSpentAmount = budget.getSpentAmount();
        double previousUsage = budget.getLimitAmount() > 0 ? (previousSpentAmount / budget.getLimitAmount()) * 100 : 0;
        budget.setSpentAmount(previousSpentAmount + amount);
        double currentUsage = budget.getLimitAmount() > 0 ? (budget.getSpentAmount() / budget.getLimitAmount()) * 100 : 0;

        if (previousUsage < budget.getAlertThreshold() && currentUsage >= budget.getAlertThreshold()) {
            String severity = currentUsage >= 100 ? "CRITICAL" : "WARNING";
            String message = "Budget '" + budget.getName() + "' has reached "
                    + String.format("%.1f", currentUsage) + "% of its limit.";
            notificationClient.sendBudgetAlert(
                    budget.getUserId(),
                    "Budget Alert",
                    message,
                    severity,
                    budget.getBudgetId()
            );
        }

        budgetRepo.save(budget);
    }

    @Override
    public BudgetProgress getBudgetProgress(Long budgetId) {
        Budget budget = budgetRepo.findByBudgetId(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found with id: " + budgetId));

        double limitAmount = budget.getLimitAmount();
        double spentAmount = budget.getSpentAmount();
        double percentageUsed = limitAmount > 0 ? (spentAmount / limitAmount) * 100 : 0;
        double remaining = limitAmount - spentAmount;
        boolean alertTriggered = percentageUsed >= budget.getAlertThreshold();

        String alertMessage = alertTriggered
                ? "⚠️ Alert: You have used " + String.format("%.1f", percentageUsed) + "% of your budget!"
                : "Budget is within safe limits.";

        return new BudgetProgress(
                budget.getBudgetId(),
                limitAmount,
                spentAmount,
                remaining,
                percentageUsed,
                alertTriggered,
                alertMessage
        );
    }

    @Override
    public List<String> checkBudgetAlerts(Long userId) {
        List<Budget> budgets = budgetRepo.findByUserIdAndIsActive(userId, true);
        List<String> alerts = new ArrayList<>();

        for (Budget budget : budgets) {
            double percentageUsed = (budget.getSpentAmount() / budget.getLimitAmount()) * 100;
            if (percentageUsed >= budget.getAlertThreshold()) {
                alerts.add("Budget '" + budget.getName() + "' has used "
                        + String.format("%.1f", percentageUsed) + "% of limit.");
            }
        }
        return alerts;
    }

    @Override
    public void resetBudgetPeriod(Long budgetId) {
        Budget budget = budgetRepo.findByBudgetId(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found with id: " + budgetId));
        budget.setSpentAmount(0.0);

        LocalDate now = LocalDate.now();
        budget.setStartDate(now);

        if (budget.getPeriod() == Budget.Period.MONTHLY) {
            budget.setEndDate(now.plusMonths(1));
        } else if (budget.getPeriod() == Budget.Period.WEEKLY) {
            budget.setEndDate(now.plusWeeks(1));
        }

        budgetRepo.save(budget);
    }

    @Override
    public Optional<Budget> getBudgetsByCategory(Long userId, Long categoryId) {
        return budgetRepo.findByUserIdAndCategoryId(userId, categoryId);
    }

    private void validateCategorySelection(Budget budget) {
        if (budget.getCategoryId() == null || budget.getCategoryId() <= 0) {
            throw new RuntimeException("Budget category is required");
        }
    }

    // Scheduled job: auto-reset at start of each month (midnight on 1st)
    @Scheduled(cron = "0 0 0 1 * *")
    public void autoResetMonthlyBudgets() {
        List<Budget> monthlyBudgets = budgetRepo.findByPeriod(Budget.Period.MONTHLY);
        for (Budget budget : monthlyBudgets) {
            if (budget.isActive()) {
                resetBudgetPeriod(budget.getBudgetId());
            }
        }
    }
}
