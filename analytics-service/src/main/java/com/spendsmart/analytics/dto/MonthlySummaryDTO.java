package com.spendsmart.analytics.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlySummaryDTO {

    private double totalIncome;
    private double totalExpenses;
    private double netSavings;
    private double savingsRate;
}