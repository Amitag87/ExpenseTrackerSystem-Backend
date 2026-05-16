package com.spendsmart.notification.repository;

import com.spendsmart.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientId(Long recipientId);

    List<Notification> findByRecipientIdAndIsRead(Long recipientId, boolean isRead);

    int countByRecipientIdAndIsRead(Long recipientId, boolean isRead);

    List<Notification> findByType(String type);

    List<Notification> findBySeverity(String severity);

    List<Notification> findByIsAcknowledged(boolean isAcknowledged);
}