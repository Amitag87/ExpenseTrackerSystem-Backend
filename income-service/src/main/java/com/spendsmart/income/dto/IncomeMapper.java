package com.spendsmart.income.dto;

import com.spendsmart.income.entity.Income;
import org.springframework.stereotype.Component;

@Component
public class IncomeMapper {

    public Income toEntity(IncomeRequest request) {
        Income income = new Income();
        income.setUserId(request.getUserId());
        income.setCategoryId(request.getCategoryId());
        income.setTitle(request.getTitle());
        income.setAmount(request.getAmount());
        income.setCurrency(request.getCurrency());
        income.setSource(request.getSource());
        income.setDate(request.getDate());
        income.setNotes(request.getNotes());
        income.setRecurring(request.isRecurring());
        income.setRecurrencePeriod(request.getRecurrencePeriod());
        return income;
    }

    public IncomeResponse toResponse(Income income) {
        IncomeResponse response = new IncomeResponse();
        response.setIncomeId(income.getIncomeId());
        response.setUserId(income.getUserId());
        response.setCategoryId(income.getCategoryId());
        response.setTitle(income.getTitle());
        response.setAmount(income.getAmount());
        response.setCurrency(income.getCurrency());
        response.setSource(income.getSource());
        response.setDate(income.getDate());
        response.setNotes(income.getNotes());
        response.setRecurring(income.isRecurring());
        response.setRecurrencePeriod(income.getRecurrencePeriod());
        response.setCreatedAt(income.getCreatedAt());
        return response;
    }
}
