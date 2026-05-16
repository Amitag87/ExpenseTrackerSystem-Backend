package com.spendsmart.notification.service;

import com.spendsmart.notification.entity.Notification;
import com.spendsmart.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the NotifService interface.
 * Handles the business logic for creating, updating, and retrieving notifications.
 */
@Service
@RequiredArgsConstructor
public class NotifServiceImpl implements NotifService {

    private final NotificationRepository repository;

    /**
     * Sends a generic notification by saving it to the repository.
     *
     * @param notification the notification entity to be saved
     */
    @Override
    public void send(Notification notification) {
        repository.save(notification);
    }

    /**
     * Sends a budget-related alert to a specific recipient.
     * The severity is set to CRITICAL if the amount exceeds 1000, otherwise WARNING.
     *
     * @param recipientId the ID of the user receiving the alert
     * @param message     the alert message
     * @param amount      the excess amount that triggered the alert
     */
    @Override
    public void sendBudgetAlert(Long recipientId, String message, double amount) {
        Notification notification = new Notification();
        notification.setRecipientId(recipientId);
        notification.setType("BUDGET_EXCEEDED");
        notification.setSeverity(amount > 1000 ? "CRITICAL" : "WARNING");
        notification.setTitle("Budget Alert");
        notification.setMessage(message);

        send(notification);
    }

    /**
     * Sends an identical notification to multiple recipients.
     *
     * @param recipients a list of recipient IDs
     * @param title      the title of the notification
     * @param message    the content of the notification
     */
    @Override
    public void sendBulk(List<Long> recipients, String title, String message) {
        for (Long id : recipients) {
            Notification n = new Notification();
            n.setRecipientId(id);
            n.setTitle(title);
            n.setMessage(message);
            n.setSeverity("INFO");
            send(n);
        }
    }

    /**
     * Marks a specific notification as read.
     *
     * @param notificationId the ID of the notification to mark as read
     */
    @Override
    public void markAsRead(Long notificationId) {
        Notification n = repository.findById(notificationId).orElseThrow();
        n.setRead(true);
        repository.save(n);
    }

    /**
     * Marks all notifications belonging to a specific recipient as read.
     *
     * @param recipientId the ID of the user whose notifications should be marked read
     */
    @Override
    public void markAllRead(Long recipientId) {
        List<Notification> list = repository.findByRecipientId(recipientId);
        list.forEach(n -> n.setRead(true));
        repository.saveAll(list);
    }

    /**
     * Acknowledges a specific notification.
     *
     * @param notificationId the ID of the notification to acknowledge
     */
    @Override
    public void acknowledge(Long notificationId) {
        Notification n = repository.findById(notificationId).orElseThrow();
        n.setAcknowledged(true);
        repository.save(n);
    }

    /**
     * Retrieves all notifications for a specific recipient.
     *
     * @param recipientId the ID of the recipient
     * @return a list of notifications belonging to the recipient
     */
    @Override
    public List<Notification> getByRecipient(Long recipientId) {
        return repository.findByRecipientId(recipientId);
    }

    /**
     * Retrieves the count of unread notifications for a specific recipient.
     *
     * @param recipientId the ID of the recipient
     * @return the number of unread notifications
     */
    @Override
    public int getUnreadCount(Long recipientId) {
        return repository.countByRecipientIdAndIsRead(recipientId, false);
    }

    /**
     * Deletes a specific notification by its ID.
     *
     * @param notificationId the ID of the notification to delete
     */
    @Override
    public void deleteNotification(Long notificationId) {
        repository.deleteById(notificationId);
    }

    /**
     * Retrieves all notifications in the system.
     *
     * @return a list of all notifications
     */
    @Override
    public List<Notification> getAll() {
        return repository.findAll();
    }
}