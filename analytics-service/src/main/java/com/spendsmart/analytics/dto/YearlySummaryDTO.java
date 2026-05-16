package com.spendsmart.analytics.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YearlySummaryDTO {

    private double totalIncome;
    private double totalExpenses;
    private double totalSavings;
}