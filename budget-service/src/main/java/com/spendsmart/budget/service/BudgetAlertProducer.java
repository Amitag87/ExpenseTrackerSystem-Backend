package com.spendsmart.budget.service;

import com.spendsmart.budget.config.RabbitMQConfig;
import com.spendsmart.budget.dto.BudgetAlertMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetAlertProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendAlert(BudgetAlertMessage message) {
        log.info("Sending budget alert to RabbitMQ: {}", message);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.BUDGET_EXCHANGE,
                RabbitMQConfig.BUDGET_ROUTING_KEY,
                message
        );
    }
}
