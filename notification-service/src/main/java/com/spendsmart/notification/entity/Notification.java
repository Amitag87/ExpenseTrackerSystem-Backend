package com.spendsmart.notification.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    private Long recipientId;

    private String type; // BUDGET_ALERT, SYSTEM, etc
    private String severity; // INFO, WARNING, CRITICAL

    private String title;
    private String message;

    private Integer relatedId;
    private String relatedType;

    private boolean isRead;
    private boolean isAcknowledged;

    private LocalDateTime createdAt = LocalDateTime.now();
}