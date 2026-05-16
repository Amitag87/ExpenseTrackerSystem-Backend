package com.spendsmart.expense.resource;

import com.spendsmart.expense.dto.ExpenseMapper;
import com.spendsmart.expense.dto.ExpenseResponse;
import com.spendsmart.expense.entity.Expense;
import com.spendsmart.expense.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpenseResource.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for unit testing
public class ExpenseResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpenseService expenseService;

    @MockBean
    private ExpenseMapper expenseMapper;

    @Test
    void getById_Success() throws Exception {
        Expense expense = new Expense();
        expense.setExpenseId(1L);
        expense.setTitle("Lunch");
        
        ExpenseResponse response = new ExpenseResponse();
        response.setExpenseId(1L);
        response.setTitle("Lunch");

        when(expenseService.getExpenseById(1L)).thenReturn(Optional.of(expense));
        when(expenseMapper.toResponse(any(Expense.class))).thenReturn(response);

        mockMvc.perform(get("/api/expenses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Lunch"));
    }

    @Test
    void getById_NotFound() throws Exception {
        when(expenseService.getExpenseById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/expenses/1"))
                .andExpect(status().isNotFound());
    }
}
