package com.spendsmart.analytics.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AnalyticsDataClient {

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${analytics.clients.income-url:http://localhost:8083}")
    private String incomeUrl;

    @Value("${analytics.clients.expense-url:http://localhost:8082}")
    private String expenseUrl;

    @Value("${analytics.clients.category-url:http://localhost:8084}")
    private String categoryUrl;

    public double getMonthlyIncome(Long userId, int year, int month) {
        String url = incomeUrl + "/api/incomes/total/month?userId=" + userId
                + "&month=" + month + "&year=" + year;
        return getDouble(url);
    }

    public double getMonthlyExpenses(Long userId, int year, int month) {
        JsonNode expenses = getJson(expenseUrl + "/api/expenses/user/" + userId
                + "/month?month=" + month + "&year=" + year);

        double total = 0;
        if (expenses != null && expenses.isArray()) {
            for (JsonNode expense : expenses) {
                total += expense.path("amount").asDouble(0);
            }
        }
        return total;
    }

    public String getTopExpenseCategory(Long userId, int year, int month) {
        Map<Long, Double> totals = monthlyExpenseTotalsByCategory(userId, year, month);
        Map<Long, String> names = categoryNames(userId);

        return totals.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> names.getOrDefault(entry.getKey(), "Uncategorized"))
                .orElse("Uncategorized");
    }

    public Map<String, Double> getExpenseBreakdownByCategory(Long userId) {
        JsonNode expenses = getJson(expenseUrl + "/api/expenses/user/" + userId);
        Map<Long, String> names = categoryNames(userId);
        Map<String, Double> result = new HashMap<>();

        if (expenses != null && expenses.isArray()) {
            for (JsonNode expense : expenses) {
                Long categoryId = expense.path("categoryId").isNumber()
                        ? expense.path("categoryId").asLong()
                        : null;
                String category = categoryId == null
                        ? "Uncategorized"
                        : names.getOrDefault(categoryId, "Uncategorized");
                result.merge(category, expense.path("amount").asDouble(0), Double::sum);
            }
        }

        return result;
    }

    private Map<Long, Double> monthlyExpenseTotalsByCategory(Long userId, int year, int month) {
        JsonNode expenses = getJson(expenseUrl + "/api/expenses/user/" + userId
                + "/month?month=" + month + "&year=" + year);
        Map<Long, Double> totals = new HashMap<>();

        if (expenses != null && expenses.isArray()) {
            for (JsonNode expense : expenses) {
                if (expense.path("categoryId").isNumber()) {
                    totals.merge(
                            expense.path("categoryId").asLong(),
                            expense.path("amount").asDouble(0),
                            Double::sum
                    );
                }
            }
        }

        return totals;
    }

    private Map<Long, String> categoryNames(Long userId) {
        JsonNode categories = getJson(categoryUrl + "/api/categories/user/" + userId);
        Map<Long, String> names = new HashMap<>();

        if (categories != null && categories.isArray()) {
            for (JsonNode category : categories) {
                if (category.path("categoryId").isNumber()) {
                    names.put(category.path("categoryId").asLong(), category.path("name").asText("Uncategorized"));
                }
            }
        }

        return names;
    }

    private double getDouble(String url) {
        JsonNode json = getJson(url);
        return json == null ? 0 : json.asDouble(0);
    }

    private JsonNode getJson(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300 || response.body().isBlank()) {
                return null;
            }

            return objectMapper.readTree(response.body());
        } catch (IOException | InterruptedException | IllegalArgumentException ex) {
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return null;
        }
    }
}
