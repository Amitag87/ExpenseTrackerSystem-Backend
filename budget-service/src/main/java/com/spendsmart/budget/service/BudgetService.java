package com.spendsmart.budget.service;

import  com.spendsmart.budget.dto.BudgetProgress;
import  com.spendsmart.budget.entity.Budget;

import java.util.List;
import java.util.Optional;

public interface BudgetService {

    Budget createBudget(Budget budget);

    Optional<Budget> getBudgetById(Long budgetId);

    List<Budget> getBudgetsByUser(Long userId);

    List<Budget> getActiveBudgets(Long userId);

    Budget updateBudget(Long budgetId, Budget budget);

    void deleteBudget(Long budgetId);

    void updateSpentAmount(Long budgetId, double amount);

    BudgetProgress getBudgetProgress(Long budgetId);

    List<String> checkBudgetAlerts(Long userId);

    void resetBudgetPeriod(Long budgetId);

    Optional<Budget> getBudgetsByCategory(Long userId, Long categoryId);
}