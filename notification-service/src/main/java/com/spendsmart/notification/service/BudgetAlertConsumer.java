package com.spendsmart.notification.service;

import com.spendsmart.notification.config.RabbitMQConfig;
import com.spendsmart.notification.dto.BudgetAlertMessage;
import com.spendsmart.notification.entity.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetAlertConsumer {

    private final NotifService notifService;

    @RabbitListener(queues = RabbitMQConfig.BUDGET_QUEUE)
    public void consumeBudgetAlert(BudgetAlertMessage message) {
        log.info("Received budget alert from RabbitMQ: {}", message);

        double usagePercentage = (message.getSpentAmount() / message.getLimitAmount()) * 100;
        String content = String.format("Budget '%s' has reached %.1f%% of its limit ($%.2f / $%.2f).",
                message.getBudgetName(), usagePercentage, message.getSpentAmount(), message.getLimitAmount());

        Notification notification = new Notification();
        notification.setRecipientId(message.getUserId());
        notification.setTitle("Budget Alert: " + message.getBudgetName());
        notification.setMessage(content);
        notification.setSeverity(message.getAlertType()); // WARNING or CRITICAL
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notifService.send(notification);
    }
}
