package com.spendsmart.recurring.dto;

import com.spendsmart.recurring.entity.RecurringTransaction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RecurringRequest {

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Category ID cannot be null")
    private Long categoryId;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @Positive(message = "Amount must be positive")
    private double amount;

    @NotNull(message = "Type cannot be null")
    private RecurringTransaction.Type type;

    @NotNull(message = "Frequency cannot be null")
    private RecurringTransaction.Frequency frequency;

    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;

    private LocalDate endDate;

    private String description;

    private String paymentMethod;
}
