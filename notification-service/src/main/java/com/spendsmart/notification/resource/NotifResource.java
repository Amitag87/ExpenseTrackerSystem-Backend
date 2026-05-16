package com.spendsmart.notification.resource;


import com.spendsmart.notification.dto.NotificationRequest;
import com.spendsmart.notification.entity.Notification;
import com.spendsmart.notification.service.NotifService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotifResource {

    private final NotifService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody NotificationRequest request) {
        Notification notification = new Notification();
        notification.setRecipientId(request.getRecipientId());
        notification.setType(request.getType());
        notification.setSeverity(request.getSeverity());
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setRelatedId(request.getRelatedId());
        notification.setRelatedType(request.getRelatedType());
        notification.setRead(Boolean.TRUE.equals(request.getRead()));
        notification.setAcknowledged(Boolean.TRUE.equals(request.getAcknowledged()));
        service.send(notification);
        return ResponseEntity.ok("Sent");
    }

    @GetMapping("/{recipientId}")
    public List<Notification> getByRecipient(@PathVariable Long recipientId) {
        return service.getByRecipient(recipientId);
    }

    @PutMapping("/read/{id}")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        service.markAsRead(id);
        return ResponseEntity.ok("Marked as read");
    }

    @PutMapping("/read/all/{recipientId}")
    public ResponseEntity<?> markAllRead(@PathVariable Long recipientId) {
        service.markAllRead(recipientId);
        return ResponseEntity.ok("All marked as read");
    }

    @PutMapping("/ack/{id}")
    public ResponseEntity<?> acknowledge(@PathVariable Long id) {
        service.acknowledge(id);
        return ResponseEntity.ok("Acknowledged");
    }

    @GetMapping("/unread-count/{recipientId}")
    public int getUnreadCount(@PathVariable Long recipientId) {
        return service.getUnreadCount(recipientId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteNotification(id);
        return ResponseEntity.ok("Deleted");
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> sendBulk(@RequestBody List<Long> users,
                                      @RequestParam String title,
                                      @RequestParam String message) {
        service.sendBulk(users, title, message);
        return ResponseEntity.ok("Bulk sent");
    }

    @GetMapping("/all")
    public List<Notification> getAll() {
        return service.getAll();
    }
}
