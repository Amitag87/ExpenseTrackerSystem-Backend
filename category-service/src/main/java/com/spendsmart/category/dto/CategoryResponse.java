package com.spendsmart.category.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CategoryResponse {
    private Long categoryId;
    private Long userId;
    private String name;
    private String type;
    private String icon;
    private String colorCode;
    private double budgetLimit;
    private boolean isDefault;
    private LocalDate createdAt;
}
