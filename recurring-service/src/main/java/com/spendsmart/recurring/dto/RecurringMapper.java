package com.spendsmart.recurring.dto;

import com.spendsmart.recurring.entity.RecurringTransaction;
import org.springframework.stereotype.Component;

/**
 * Mapper class to convert between RecurringTransaction entities and Data Transfer Objects (DTOs).
 * This ensures that the API layer interacts with DTOs rather than exposing internal entities.
 */
@Component
public class RecurringMapper {

    /**
     * Converts a RecurringRequest DTO into a RecurringTransaction entity.
     *
     * @param request the incoming request containing recurring transaction details
     * @return a new RecurringTransaction entity populated with the request data
     */
    public RecurringTransaction toEntity(RecurringRequest request) {
        RecurringTransaction rt = new RecurringTransaction();
        rt.setUserId(request.getUserId());
        rt.setCategoryId(request.getCategoryId());
        rt.setTitle(request.getTitle());
        rt.setAmount(request.getAmount());
        rt.setType(request.getType());
        rt.setFrequency(request.getFrequency());
        rt.setStartDate(request.getStartDate());
        rt.setEndDate(request.getEndDate());
        rt.setDescription(request.getDescription());
        rt.setPaymentMethod(request.getPaymentMethod());
        return rt;
    }

    /**
     * Converts a RecurringTransaction entity into a RecurringResponse DTO.
     *
     * @param rt the recurring transaction entity from the database
     * @return a RecurringResponse DTO populated with the entity data
     */
    public RecurringResponse toResponse(RecurringTransaction rt) {
        RecurringResponse response = new RecurringResponse();
        response.setRecurringId(rt.getRecurringId());
        response.setUserId(rt.getUserId());
        response.setCategoryId(rt.getCategoryId());
        response.setTitle(rt.getTitle());
        response.setAmount(rt.getAmount());
        response.setType(rt.getType());
        response.setFrequency(rt.getFrequency());
        response.setStartDate(rt.getStartDate());
        response.setEndDate(rt.getEndDate());
        response.setNextDueDate(rt.getNextDueDate());
        response.setActive(rt.isActive());
        response.setDescription(rt.getDescription());
        response.setPaymentMethod(rt.getPaymentMethod());
        return response;
    }
}
