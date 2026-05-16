package com.spendsmart.notification.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private Long recipientId;
    private String type;
    private String severity;
    private String title;
    private String message;
    private Integer relatedId;
    private String relatedType;
    private Boolean read;
    private Boolean acknowledged;
}
