package com.spendsmart.expense.dto;

import com.spendsmart.expense.entity.Expense;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Expense entity and DTOs
 */
@Component
public class ExpenseMapper {

    /**
     * Convert Expense entity to ExpenseResponse DTO
     */
    public ExpenseResponse toResponse(Expense expense) {
        if (expense == null) {
            return null;
        }
        return ExpenseResponse.builder()
                .expenseId(expense.getExpenseId())
                .userId(expense.getUserId())
                .categoryId(expense.getCategoryId())
                .title(expense.getTitle())
                .amount(expense.getAmount())
                .currency(expense.getCurrency())
                .type(expense.getType())
                .paymentMethod(expense.getPaymentMethod())
                .date(expense.getDate())
                .notes(expense.getNotes())
                .receiptUrl(expense.getReceiptUrl())
                .isRecurring(expense.isRecurring())
                .createdAt(expense.getCreatedAt())
                .updatedAt(expense.getUpdatedAt())
                .build();
    }

    /**
     * Convert ExpenseRequest DTO to Expense entity
     */
    public Expense toEntity(ExpenseRequest request) {
        if (request == null) {
            return null;
        }
        return Expense.builder()
                .userId(request.getUserId())
                .categoryId(request.getCategoryId())
                .title(request.getTitle())
                .amount(request.getAmount())
                .currency(request.getCurrency() != null ? request.getCurrency() : "INR")
                .type(request.getType() != null ? request.getType() : "EXPENSE")
                .paymentMethod(request.getPaymentMethod())
                .date(request.getDate())
                .notes(request.getNotes())
                .receiptUrl(request.getReceiptUrl())
                .isRecurring(request.isRecurring())
                .build();
    }

    /**
     * Update existing Expense entity with ExpenseRequest DTO data
     */
    public Expense updateEntity(Expense expense, ExpenseRequest request) {
        if (request == null) {
            return expense;
        }
        expense.setUserId(request.getUserId());
        expense.setCategoryId(request.getCategoryId());
        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCurrency(request.getCurrency() != null ? request.getCurrency() : "INR");
        expense.setType(request.getType() != null ? request.getType() : "EXPENSE");
        expense.setPaymentMethod(request.getPaymentMethod());
        expense.setDate(request.getDate());
        expense.setNotes(request.getNotes());
        expense.setReceiptUrl(request.getReceiptUrl());
        expense.setRecurring(request.isRecurring());
        return expense;
    }
}
