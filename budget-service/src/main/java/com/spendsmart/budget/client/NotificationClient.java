package com.spendsmart.budget.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationClient {

    private static final String BASE_URL = "http://localhost:8088/api/notifications";

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendBudgetAlert(Long recipientId, String title, String message, String severity, Long relatedId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("recipientId", recipientId);
        payload.put("type", "BUDGET_ALERT");
        payload.put("severity", severity);
        payload.put("title", title);
        payload.put("message", message);
        payload.put("relatedId", relatedId != null ? relatedId.intValue() : null);
        payload.put("relatedType", "BUDGET");

        try {
            restTemplate.postForEntity(BASE_URL, payload, String.class);
        } catch (RestClientException ignored) {
            // Notifications should not block budget updates.
        }
    }
}
