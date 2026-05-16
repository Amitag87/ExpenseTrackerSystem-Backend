package com.spendsmart.expense.service;

import com.spendsmart.expense.entity.Expense;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseService {

    Expense addExpense(Expense expense);

    Optional<Expense> getExpenseById(Long expenseId);

    List<Expense> getExpensesByUser(Long userId);

    List<Expense> getExpensesByCategory(Long userId, Long categoryId);

    List<Expense> getExpensesByDateRange(Long userId, LocalDate start, LocalDate end);

    List<Expense> getExpensesByMonth(Long userId, int month, int year);

    Expense updateExpense(Long expenseId, Expense updatedExpense);

    void deleteExpense(Long expenseId);

    Double getTotalByUser(Long userId);

    Double getTotalByCategory(Long userId, Long categoryId);

    List<Expense> getExpensesByType(Long userId, String type);

    List<Expense> searchExpenses(Long userId, String keyword);
}