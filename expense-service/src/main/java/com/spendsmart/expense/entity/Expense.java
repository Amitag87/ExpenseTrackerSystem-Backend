package com.spendsmart.expense.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    @Column(nullable = false)
    private Long userId;

    private Long categoryId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private double amount;

    private String currency = "INR";

    // EXPENSE or SPLIT
    private String type = "EXPENSE";

    // CASH / CARD / UPI / BANK / WALLET
    private String paymentMethod;

    @Column(nullable = false)
    private LocalDate date;

    private String notes;

    private String receiptUrl;

    private boolean isRecurring = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}