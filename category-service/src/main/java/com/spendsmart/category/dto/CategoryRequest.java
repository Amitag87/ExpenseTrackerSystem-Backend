package com.spendsmart.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Type cannot be blank (EXPENSE or INCOME)")
    private String type;

    private String icon;

    private String colorCode;

    private double budgetLimit;

    private boolean isDefault;
}
