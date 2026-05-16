package com.spendsmart.category.dto;

import com.spendsmart.category.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest request) {
        Category category = new Category();
        category.setUserId(request.getUserId());
        category.setName(request.getName());
        category.setType(request.getType());
        category.setIcon(request.getIcon());
        category.setColorCode(request.getColorCode());
        category.setBudgetLimit(request.getBudgetLimit());
        category.setDefault(request.isDefault());
        return category;
    }

    public CategoryResponse toResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setCategoryId(category.getCategoryId());
        response.setUserId(category.getUserId());
        response.setName(category.getName());
        response.setType(category.getType());
        response.setIcon(category.getIcon());
        response.setColorCode(category.getColorCode());
        response.setBudgetLimit(category.getBudgetLimit());
        response.setDefault(category.isDefault());
        response.setCreatedAt(category.getCreatedAt());
        return response;
    }
}
