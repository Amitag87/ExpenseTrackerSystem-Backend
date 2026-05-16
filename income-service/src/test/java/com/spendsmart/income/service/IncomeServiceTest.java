package com.spendsmart.income.service;

import com.spendsmart.income.entity.Income;
import com.spendsmart.income.exception.ResourceNotFoundException;
import com.spendsmart.income.repository.IncomeRepository;
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
public class IncomeServiceTest {

    @Mock
    private IncomeRepository repo;

    @InjectMocks
    private IncomeServiceImpl service;

    private Income sampleIncome;

    @BeforeEach
    void setUp() {
        sampleIncome = new Income();
        sampleIncome.setIncomeId(1L);
        sampleIncome.setUserId(100L);
        sampleIncome.setAmount(1000.0);
        sampleIncome.setTitle("Salary");
    }

    @Test
    void addIncome_Success() {
        when(repo.save(any(Income.class))).thenReturn(sampleIncome);
        
        Income result = service.addIncome(sampleIncome);
        
        assertNotNull(result);
        assertEquals(1000.0, result.getAmount());
        verify(repo, times(1)).save(sampleIncome);
    }

    @Test
    void getIncomeById_Found() {
        when(repo.findById(1L)).thenReturn(Optional.of(sampleIncome));
        
        Income result = service.getIncomeById(1L);
        
        assertNotNull(result);
        assertEquals(1L, result.getIncomeId());
    }

    @Test
    void getIncomeById_NotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> service.getIncomeById(1L));
    }
}
