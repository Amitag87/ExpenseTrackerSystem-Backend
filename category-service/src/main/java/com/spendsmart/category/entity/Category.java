package com.spendsmart.category.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // EXPENSE or INCOME

    private String icon;       // emoji or icon code

    private String colorCode;  // hex color e.g. #FF5733

    private double budgetLimit;

    private boolean isDefault;

    private LocalDate createdAt;

    // ─── Constructors ───────────────────────────────────────────────────────

    public Category() {
        this.createdAt = LocalDate.now();
    }

    public Category(Long userId, String name, String type, String icon,
                    String colorCode, double budgetLimit, boolean isDefault) {
        this.userId      = userId;
        this.name        = name;
        this.type        = type;
        this.icon        = icon;
        this.colorCode   = colorCode;
        this.budgetLimit = budgetLimit;
        this.isDefault   = isDefault;
        this.createdAt   = LocalDate.now();
    }

    // ─── Getters & Setters ──────────────────────────────────────────────────

    public Long getCategoryId()                   { return categoryId; }
    public void setCategoryId(Long categoryId)    { this.categoryId = categoryId; }

    public Long getUserId()                       { return userId; }
    public void setUserId(Long userId)            { this.userId = userId; }

    public String getName()                  { return name; }
    public void setName(String name)         { this.name = name; }

    public String getType()                  { return type; }
    public void setType(String type)         { this.type = type; }

    public String getIcon()                  { return icon; }
    public void setIcon(String icon)         { this.icon = icon; }

    public String getColorCode()             { return colorCode; }
    public void setColorCode(String colorCode){ this.colorCode = colorCode; }

    public double getBudgetLimit()           { return budgetLimit; }
    public void setBudgetLimit(double budgetLimit){ this.budgetLimit = budgetLimit; }

    public boolean isDefault()               { return isDefault; }
    public void setDefault(boolean isDefault){ this.isDefault = isDefault; }

    public LocalDate getCreatedAt()          { return createdAt; }
    public void setCreatedAt(LocalDate createdAt){ this.createdAt = createdAt; }

    // ─── Utility ────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", icon='" + icon + '\'' +
                ", colorCode='" + colorCode + '\'' +
                ", budgetLimit=" + budgetLimit +
                ", isDefault=" + isDefault +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, userId, name, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Category)) return false;
        Category other = (Category) obj;
        return categoryId == other.categoryId &&
               userId     == other.userId     &&
               Objects.equals(name, other.name) &&
               Objects.equals(type, other.type);
    }
}