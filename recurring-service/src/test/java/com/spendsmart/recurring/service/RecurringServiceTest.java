package com.spendsmart.recurring.service;

import com.spendsmart.recurring.client.ExpenseClient;
import com.spendsmart.recurring.client.NotificationClient;
import com.spendsmart.recurring.entity.RecurringTransaction;
import com.spendsmart.recurring.repository.RecurringRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecurringServiceTest {

    @Mock
    private RecurringRepository repo;

    @Mock
    private ExpenseClient expenseClient;

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private RecurringServiceImpl service;

    private RecurringTransaction sampleRT;

    @BeforeEach
    void setUp() {
        sampleRT = new RecurringTransaction();
        sampleRT.setRecurringId(1L);
        sampleRT.setUserId(100L);
        sampleRT.setTitle("Netflix");
        sampleRT.setAmount(15.99);
        sampleRT.setFrequency(RecurringTransaction.Frequency.MONTHLY);
        sampleRT.setNextDueDate(LocalDate.now());
        sampleRT.setActive(true);
        sampleRT.setType(RecurringTransaction.Type.EXPENSE);
    }

    @Test
    void addRecurring_Success() {
        when(repo.save(any(RecurringTransaction.class))).thenReturn(sampleRT);
        
        RecurringTransaction result = service.addRecurring(sampleRT);
        
        assertNotNull(result);
        assertTrue(result.isActive());
        verify(repo, times(1)).save(sampleRT);
    }

    @Test
    void updateNextDueDate_Monthly() {
        when(repo.findById(1L)).thenReturn(Optional.of(sampleRT));
        LocalDate originalDate = sampleRT.getNextDueDate();
        
        service.updateNextDueDate(1L);
        
        assertEquals(originalDate.plusMonths(1), sampleRT.getNextDueDate());
        verify(repo, times(1)).save(sampleRT);
    }

    @Test
    void generateTransactionFromRecurring_CallsClient() {
        service.generateTransactionFromRecurring(sampleRT);
        verify(expenseClient, times(1)).createTransaction(sampleRT);
    }
}
