package com.spendsmart.income.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class IncomeResponse {
    private Long incomeId;
    private Long userId;
    private Long categoryId;
    private String title;
    private double amount;
    private String currency;
    private String source;
    private LocalDate date;
    private String notes;
    private boolean isRecurring;
    private String recurrencePeriod;
    private LocalDateTime createdAt;
}
