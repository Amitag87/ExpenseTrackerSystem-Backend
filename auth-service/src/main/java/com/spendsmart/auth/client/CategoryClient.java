package com.spendsmart.auth.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class CategoryClient {

    private final RestTemplate restTemplate;
    private final String categoryServiceUrl;

    public CategoryClient(RestTemplate restTemplate,
                          @Value("${category.service.url:http://localhost:8084}") String categoryServiceUrl) {
        this.restTemplate = restTemplate;
        this.categoryServiceUrl = categoryServiceUrl;
    }

    public void initDefaultCategories(Long userId) {
        try {
            restTemplate.postForEntity(
                    categoryServiceUrl + "/api/categories/user/" + userId + "/defaults",
                    null,
                    Void.class
            );
        } catch (RestClientException ex) {
            throw new RuntimeException("Unable to initialize default categories for user " + userId, ex);
        }
    }
}
