package com.spendsmart.budget.service;

import com.spendsmart.budget.dto.BudgetAlertMessage;
import com.spendsmart.budget.entity.Budget;
import com.spendsmart.budget.repository.BudgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepo;

    @Mock
    private BudgetAlertProducer alertProducer;

    @InjectMocks
    private BudgetServiceImpl budgetService;

    private Budget sampleBudget;

    @BeforeEach
    void setUp() {
        sampleBudget = new Budget();
        sampleBudget.setBudgetId(1L);
        sampleBudget.setUserId(100L);
        sampleBudget.setCategoryId(200L);
        sampleBudget.setLimitAmount(1000.0);
        sampleBudget.setSpentAmount(500.0);
        sampleBudget.setAlertThreshold(80);
        sampleBudget.setName("Monthly Groceries");
    }

    @Test
    void createBudget_Success() {
        when(budgetRepo.save(any(Budget.class))).thenReturn(sampleBudget);
        
        Budget result = budgetService.createBudget(sampleBudget);
        
        assertNotNull(result);
        assertEquals(0.0, result.getSpentAmount());
        verify(budgetRepo, times(1)).save(sampleBudget);
    }

    @Test
    void updateSpentAmount_TriggersAlert() {
        when(budgetRepo.findByBudgetId(1L)).thenReturn(Optional.of(sampleBudget));
        
        // current spent 500, limit 1000 -> 50%
        // add 400 -> 900 -> 90% (threshold 80%)
        budgetService.updateSpentAmount(1L, 400.0);
        
        ArgumentCaptor<BudgetAlertMessage> alertCaptor = ArgumentCaptor.forClass(BudgetAlertMessage.class);
        verify(alertProducer, times(1)).sendAlert(alertCaptor.capture());

        BudgetAlertMessage alert = alertCaptor.getValue();
        assertEquals(100L, alert.getUserId());
        assertEquals("Monthly Groceries", alert.getBudgetName());
        assertEquals(900.0, alert.getSpentAmount());
        assertEquals(1000.0, alert.getLimitAmount());
        assertEquals("WARNING", alert.getAlertType());
        verify(budgetRepo, times(1)).save(sampleBudget);
        assertEquals(900.0, sampleBudget.getSpentAmount());
    }

    @Test
    void getBudgetById_Found() {
        when(budgetRepo.findByBudgetId(1L)).thenReturn(Optional.of(sampleBudget));
        
        Optional<Budget> result = budgetService.getBudgetById(1L);
        
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getBudgetId());
    }
}
