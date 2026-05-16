package com.spendsmart.notification.service;

import com.spendsmart.notification.entity.Notification;
import java.util.List;

public interface NotifService {

    void send(Notification notification);

    void sendBudgetAlert(Long recipientId, String message, double amount);

    void sendBulk(List<Long> recipients, String title, String message);

    void markAsRead(Long notificationId);

    void markAllRead(Long recipientId);

    void acknowledge(Long notificationId);

    List<Notification> getByRecipient(Long recipientId);

    int getUnreadCount(Long recipientId);

    void deleteNotification(Long notificationId);

    List<Notification> getAll();
}