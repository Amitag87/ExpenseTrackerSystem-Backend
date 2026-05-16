package com.spendsmart.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetAlertMessage implements Serializable {
    private Long userId;
    private String budgetName;
    private Double limitAmount;
    private Double spentAmount;
    private String alertType;
}
