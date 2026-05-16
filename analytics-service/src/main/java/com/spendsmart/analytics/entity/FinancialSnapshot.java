package com.spendsmart.analytics.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long snapshotId;

    private Long userId;
    private String period;
    private int year;
    private int month;

    private double totalIncome;
    private double totalExpenses;
    private double netSavings;
    private double savingsRate;

    private String topCategory;

    private LocalDateTime createdAt;
}
