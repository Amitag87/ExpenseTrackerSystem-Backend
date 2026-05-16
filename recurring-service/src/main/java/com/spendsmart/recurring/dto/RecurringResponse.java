package com.spendsmart.recurring.dto;

import com.spendsmart.recurring.entity.RecurringTransaction;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RecurringResponse {
    private Long recurringId;
    private Long userId;
    private Long categoryId;
    private String title;
    private double amount;
    private RecurringTransaction.Type type;
    private RecurringTransaction.Frequency frequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nextDueDate;
    private boolean isActive;
    private String description;
    private String paymentMethod;
}
