package com.spendsmart.income.service;

import com.spendsmart.income.entity.Income;

import java.time.LocalDate;
import java.util.List;

public interface IncomeService {

    Income addIncome(Income income);

    Income getIncomeById(Long id);

    List<Income> getIncomesByUser(Long userId);

    List<Income> getIncomesBySource(Long userId, String source);

    List<Income> getIncomesByDateRange(Long userId, LocalDate start, LocalDate end);

    List<Income> getIncomesByMonth(Long userId, int month, int year);

    Income updateIncome(Long id, Income income);

    void deleteIncome(Long id);

    Double getTotalIncomeByUser(Long userId);

    // ✅ NEW - missing from earlier version
    Double getTotalIncomeByMonth(Long userId, int month, int year);

    List<Income> getRecurringIncomes();
}