package com.spendsmart.budget.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetProgress {
    private Long budgetId;
    private double limitAmount;
    private double spentAmount;
    private double remainingAmount;
    private double percentageUsed;
    private boolean alertTriggered;
    private String alertMessage;
}
