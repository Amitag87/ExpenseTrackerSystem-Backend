package com.spendsmart.recurring.client;



import com.spendsmart.recurring.entity.RecurringTransaction;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExpenseClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public void createTransaction(RecurringTransaction rt) {
        String url = "http://EXPENSE-SERVICE/api/transactions";
        restTemplate.postForObject(url, rt, String.class);
    }
}