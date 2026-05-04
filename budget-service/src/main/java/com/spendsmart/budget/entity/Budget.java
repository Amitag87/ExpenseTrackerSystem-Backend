package com.spendsmart.budget.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "budgets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long budgetId;

    private Long userId;
    private Long categoryId;
    private String name;
    private double limitAmount;
    private String currency;

    @Enumerated(EnumType.STRING)
    private Period period;

    private LocalDate startDate;
    private LocalDate endDate;
    private double spentAmount;
    private int alertThreshold;
    private boolean isActive;

    public enum Period {
        MONTHLY, WEEKLY, CUSTOM
    }
}
