package com.spendsmart.analytics.service;

import com.spendsmart.analytics.client.AnalyticsDataClient;
import com.spendsmart.analytics.entity.FinancialSnapshot;
import com.spendsmart.analytics.repository.AnalyticsRepository;
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
public class AnalyticsServiceTest {

    @Mock
    private AnalyticsRepository repo;

    @Mock
    private AnalyticsDataClient dataClient;

    @InjectMocks
    private AnalyticsServiceImpl service;

    private FinancialSnapshot sampleSnapshot;

    @BeforeEach
    void setUp() {
        sampleSnapshot = new FinancialSnapshot();
        sampleSnapshot.setSnapshotId(1L);
        sampleSnapshot.setUserId(100L);
        sampleSnapshot.setYear(2024);
        sampleSnapshot.setMonth(5);
        sampleSnapshot.setTotalIncome(5000.0);
        sampleSnapshot.setTotalExpenses(3000.0);
        sampleSnapshot.setNetSavings(2000.0);
    }

    @Test
    void generateMonthlySnapshot_Success() {
        when(repo.findByUserIdAndYearAndMonth(100L, 2024, 5)).thenReturn(Optional.empty());
        when(dataClient.getMonthlyIncome(100L, 2024, 5)).thenReturn(5000.0);
        when(dataClient.getMonthlyExpenses(100L, 2024, 5)).thenReturn(3000.0);
        when(dataClient.getTopExpenseCategory(100L, 2024, 5)).thenReturn("Food");
        when(repo.save(any(FinancialSnapshot.class))).thenReturn(sampleSnapshot);
        
        FinancialSnapshot result = service.generateMonthlySnapshot(100L, 2024, 5);
        
        assertNotNull(result);
        verify(repo, times(1)).save(any(FinancialSnapshot.class));
    }

    @Test
    void getMonthlySummary_Success() {
        when(repo.findByUserIdAndYearAndMonth(100L, 2024, 5)).thenReturn(Optional.of(sampleSnapshot));
        
        var result = service.getMonthlySummary(100L, 2024, 5);
        
        assertEquals(5000.0, result.get("income"));
        assertEquals(3000.0, result.get("expense"));
    }

    @Test
    void deleteSnapshot_Success() {
        when(repo.existsById(1L)).thenReturn(true);
        
        service.deleteSnapshot(1L);
        
        verify(repo, times(1)).deleteById(1L);
    }
}
