package com.spendsmart.expense.service;

import com.spendsmart.expense.client.BudgetClient;
import com.spendsmart.expense.entity.Expense;
import com.spendsmart.expense.repository.ExpenseRepository;
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
public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private BudgetClient budgetClient;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    private Expense sampleExpense;

    @BeforeEach
    void setUp() {
        sampleExpense = new Expense();
        sampleExpense.setExpenseId(1L);
        sampleExpense.setUserId(100L);
        sampleExpense.setCategoryId(200L);
        sampleExpense.setAmount(500.0);
        sampleExpense.setTitle("Test Expense");
    }

    @Test
    void addExpense_Success() {
        when(expenseRepository.save(any(Expense.class))).thenReturn(sampleExpense);
        
        Expense result = expenseService.addExpense(sampleExpense);
        
        assertNotNull(result);
        assertEquals(sampleExpense.getAmount(), result.getAmount());
        verify(expenseRepository, times(1)).save(sampleExpense);
        verify(budgetClient, times(1)).updateSpentAmount(100L, 200L, 500.0);
    }

    @Test
    void getExpenseById_Found() {
        when(expenseRepository.findByExpenseId(1L)).thenReturn(Optional.of(sampleExpense));
        
        Optional<Expense> result = expenseService.getExpenseById(1L);
        
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getExpenseId());
    }

    @Test
    void deleteExpense_Success() {
        when(expenseRepository.findByExpenseId(1L)).thenReturn(Optional.of(sampleExpense));
        
        expenseService.deleteExpense(1L);
        
        verify(expenseRepository, times(1)).deleteByExpenseId(1L);
        verify(budgetClient, times(1)).updateSpentAmount(100L, 200L, -500.0);
    }
}
