package com.spendsmart.budget.repository;

import com.spendsmart.budget.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUserId(Long userId);

    Optional<Budget> findByBudgetId(Long budgetId);

    Optional<Budget> findByUserIdAndCategoryId(Long userId, Long categoryId);

    List<Budget> findByPeriod(Budget.Period period);

    List<Budget> findByIsActive(boolean isActive);

    List<Budget> findByUserIdAndIsActive(Long userId, boolean isActive);

    long countByUserId(Long userId);

    void deleteByBudgetId(Long budgetId);
}