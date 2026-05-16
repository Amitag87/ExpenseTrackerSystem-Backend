package com.spendsmart.notification.service;

import com.spendsmart.notification.entity.Notification;
import com.spendsmart.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotifServiceTest {

    @Mock
    private NotificationRepository repository;

    @InjectMocks
    private NotifServiceImpl service;

    private Notification sampleNotification;

    @BeforeEach
    void setUp() {
        sampleNotification = new Notification();
        sampleNotification.setNotificationId(1L);
        sampleNotification.setRecipientId(100L);
        sampleNotification.setTitle("Test Alert");
        sampleNotification.setRead(false);
    }

    @Test
    void send_Success() {
        service.send(sampleNotification);
        verify(repository, times(1)).save(sampleNotification);
    }

    @Test
    void markAsRead_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(sampleNotification));
        
        service.markAsRead(1L);
        
        assertTrue(sampleNotification.isRead());
        verify(repository, times(1)).save(sampleNotification);
    }

    @Test
    void sendBudgetAlert_Success() {
        service.sendBudgetAlert(100L, "Over budget", 1500.0);
        
        verify(repository, times(1)).save(argThat(n -> 
            n.getRecipientId().equals(100L) && 
            n.getSeverity().equals("CRITICAL") &&
            n.getType().equals("BUDGET_EXCEEDED")
        ));
    }
}
