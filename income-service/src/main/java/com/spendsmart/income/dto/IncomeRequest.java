package com.spendsmart.income.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IncomeRequest {

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Category ID cannot be null")
    private Long categoryId;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @Positive(message = "Amount must be positive")
    private double amount;

    @NotBlank(message = "Currency cannot be blank")
    private String currency;

    @NotBlank(message = "Source cannot be blank")
    private String source;

    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    private String notes;

    private boolean isRecurring;

    private String recurrencePeriod;
}
