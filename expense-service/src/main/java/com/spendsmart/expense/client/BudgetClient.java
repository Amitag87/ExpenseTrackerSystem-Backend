package com.spendsmart.expense.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class BudgetClient {

    private static final String BASE_URL = "http://localhost:8085/api/budgets";

    private final RestTemplate restTemplate = new RestTemplate();

    public void updateSpentAmount(Long userId, Long categoryId, double amountDelta) {
        if (userId == null || categoryId == null || amountDelta == 0) {
            return;
        }

        Optional<Long> budgetId = findBudgetIdByCategory(userId, categoryId);
        if (budgetId.isEmpty()) {
            return;
        }

        try {
            restTemplate.postForEntity(
                    BASE_URL + "/" + budgetId.get() + "/spent?amount=" + amountDelta,
                    null,
                    Void.class
            );
        } catch (RestClientException ignored) {
            // Budget sync should not block expense operations.
        }
    }

    private Optional<Long> findBudgetIdByCategory(Long userId, Long categoryId) {
        try {
            BudgetSummary response = restTemplate.getForObject(
                    BASE_URL + "/user/" + userId + "/category/" + categoryId,
                    BudgetSummary.class
            );
            return Optional.ofNullable(response != null ? response.getBudgetId() : null);
        } catch (HttpClientErrorException.NotFound ignored) {
            return Optional.empty();
        } catch (RestClientException ignored) {
            return Optional.empty();
        }
    }

    public static class BudgetSummary {
        private Long budgetId;

        public Long getBudgetId() {
            return budgetId;
        }

        public void setBudgetId(Long budgetId) {
            this.budgetId = budgetId;
        }
    }
}
