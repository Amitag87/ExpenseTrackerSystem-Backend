package com.spendsmart.analytics.repository;


import  com.spendsmart.analytics.entity.FinancialSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface AnalyticsRepository extends JpaRepository<FinancialSnapshot, Long> {

    List<FinancialSnapshot> findByUserId(Long userId);

    Optional<FinancialSnapshot> findByUserIdAndYearAndMonth(Long userId, int year, int month);

    List<FinancialSnapshot> findByUserIdAndYear(Long userId, int year);

    @Query("SELECT AVG(f.savingsRate) FROM FinancialSnapshot f WHERE f.userId = :userId")
    Double avgSavingsRateByUserId(Long userId);

    @Query("SELECT f.topCategory, SUM(f.totalExpenses) FROM FinancialSnapshot f WHERE f.userId = :userId GROUP BY f.topCategory")
    List<Object[]> sumExpensesByCategory(Long userId);

    @Query("SELECT f FROM FinancialSnapshot f WHERE f.userId = :userId ORDER BY f.totalExpenses DESC")
    List<FinancialSnapshot> findTopSpendingMonths(Long userId);

    int countByUserId(Long userId);
}