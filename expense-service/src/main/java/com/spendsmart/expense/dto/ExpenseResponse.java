package com.spendsmart.expense.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for returning expense data in API responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseResponse {

    private Long expenseId;

    private Long userId;

    private Long categoryId;

    private String title;

    private double amount;

    private String currency;

    private String type;

    private String paymentMethod;

    private LocalDate date;

    private String notes;

    private String receiptUrl;

    private boolean isRecurring;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
