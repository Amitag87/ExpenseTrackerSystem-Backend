package com.spendsmart.recurring.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecurringTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recurringId;

    private Long userId;
    private Long categoryId;

    private String title;
    private double amount;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nextDueDate;

    private boolean isActive;

    private String description;
    private String paymentMethod;

    public enum Type {
        EXPENSE, INCOME
    }

    public enum Frequency {
        DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY
    }
}