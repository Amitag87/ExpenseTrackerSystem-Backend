package com.spendsmart.income.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "incomes")
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private LocalDateTime createdAt = LocalDateTime.now();

    // ================= GETTERS =================

    public Long getIncomeId() {
        return incomeId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getSource() {
        return source;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public String getRecurrencePeriod() {
        return recurrencePeriod;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // ================= SETTERS =================

    public void setIncomeId(Long incomeId) {
        this.incomeId = incomeId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public void setRecurrencePeriod(String recurrencePeriod) {
        this.recurrencePeriod = recurrencePeriod;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}