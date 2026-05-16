package com.spendsmart.recurring.client;


import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationClient {

    private static final String BASE_URL = "http://localhost:8088/api/notifications";

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendReminder(Long userId, String title, String message, Long relatedId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("recipientId", userId);
        payload.put("type", "RECURRING_REMINDER");
        payload.put("severity", "INFO");
        payload.put("title", title);
        payload.put("message", message);
        payload.put("relatedId", relatedId != null ? relatedId.intValue() : null);
        payload.put("relatedType", "RECURRING");

        try {
            restTemplate.postForEntity(BASE_URL, payload, String.class);
        } catch (RestClientException ignored) {
            // Notifications should not block recurring processing.
        }
    }
}
