package com.spendsmart.expense.resource;

import com.spendsmart.expense.dto.ExpenseMapper;
import com.spendsmart.expense.dto.ExpenseRequest;
import com.spendsmart.expense.dto.ExpenseResponse;
import com.spendsmart.expense.entity.Expense;
import com.spendsmart.expense.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Base URL: /api/expenses
 *
 * POST   /api/expenses                          → Add expense
 * GET    /api/expenses/{id}                     → By expenseId
 * GET    /api/expenses/user/{userId}            → All by user
 * GET    /api/expenses/user/{userId}/category/{categoryId}  → By category
 * GET    /api/expenses/user/{userId}/range?start=&end=      → By date range
 * GET    /api/expenses/user/{userId}/month?month=&year=     → By month/year
 * GET    /api/expenses/user/{userId}/type?type=             → By type
 * GET    /api/expenses/user/{userId}/search?keyword=        → Search
 * GET    /api/expenses/user/{userId}/total                  → Total amount
 * GET    /api/expenses/user/{userId}/total/category/{categoryId} → Total by category
 * PUT    /api/expenses/{id}                     → Update
 * DELETE /api/expenses/{id}                     → Delete
 */
@RestController
@RequestMapping("/api/expenses")
public class ExpenseResource {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseMapper expenseMapper;

    // ── Add ────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<ExpenseResponse> add(@Valid @RequestBody ExpenseRequest request) {
        Expense expense = expenseMapper.toEntity(request);
        Expense savedExpense = expenseService.addExpense(expense);
        return ResponseEntity.ok(expenseMapper.toResponse(savedExpense));
    }

    // ── Get by ID ──────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getById(@PathVariable Long id) {
        return expenseService.getExpenseById(id)
                .map(expense -> ResponseEntity.ok(expenseMapper.toResponse(expense)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ── Get by User ────────────────────────────────────────────
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExpenseResponse>> getByUser(@PathVariable Long userId) {
        List<Expense> expenses = expenseService.getExpensesByUser(userId);
        List<ExpenseResponse> responses = expenses.stream()
                .map(expenseMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // ── Get by Category ────────────────────────────────────────
    @GetMapping("/user/{userId}/category/{categoryId}")
    public ResponseEntity<List<ExpenseResponse>> getByCategory(@PathVariable Long userId,
                                                        @PathVariable Long categoryId) {
        List<Expense> expenses = expenseService.getExpensesByCategory(userId, categoryId);
        List<ExpenseResponse> responses = expenses.stream()
                .map(expenseMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // ── Get by Date Range ──────────────────────────────────────
    @GetMapping("/user/{userId}/range")
    public ResponseEntity<List<ExpenseResponse>> getByDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<Expense> expenses = expenseService.getExpensesByDateRange(userId, start, end);
        List<ExpenseResponse> responses = expenses.stream()
                .map(expenseMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // ── Get by Month/Year ──────────────────────────────────────
    @GetMapping("/user/{userId}/month")
    public ResponseEntity<List<ExpenseResponse>> getByMonth(@PathVariable Long userId,
                                                     @RequestParam int month,
                                                     @RequestParam int year) {
        List<Expense> expenses = expenseService.getExpensesByMonth(userId, month, year);
        List<ExpenseResponse> responses = expenses.stream()
                .map(expenseMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // ── Get by Type ────────────────────────────────────────────
    @GetMapping("/user/{userId}/type")
    public ResponseEntity<List<ExpenseResponse>> getByType(@PathVariable Long userId,
                                                    @RequestParam String type) {
        List<Expense> expenses = expenseService.getExpensesByType(userId, type);
        List<ExpenseResponse> responses = expenses.stream()
                .map(expenseMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // ── Search ─────────────────────────────────────────────────
    @GetMapping("/user/{userId}/search")
    public ResponseEntity<List<ExpenseResponse>> search(@PathVariable Long userId,
                                                 @RequestParam String keyword) {
        List<Expense> expenses = expenseService.searchExpenses(userId, keyword);
        List<ExpenseResponse> responses = expenses.stream()
                .map(expenseMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // ── Total by User ──────────────────────────────────────────
    @GetMapping("/user/{userId}/total")
    public ResponseEntity<Double> getTotal(@PathVariable Long userId) {
        return ResponseEntity.ok(expenseService.getTotalByUser(userId));
    }

    // ── Total by Category ──────────────────────────────────────
    @GetMapping("/user/{userId}/total/category/{categoryId}")
    public ResponseEntity<Double> getTotalByCategory(@PathVariable Long userId,
                                                      @PathVariable Long categoryId) {
        return ResponseEntity.ok(expenseService.getTotalByCategory(userId, categoryId));
    }

    // ── Update ─────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> update(@PathVariable Long id,
                                           @Valid @RequestBody ExpenseRequest request) {
        Expense expense = expenseService.updateExpense(id, expenseMapper.toEntity(request));
        return ResponseEntity.ok(expenseMapper.toResponse(expense));
    }

    // ── Delete ─────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok(Map.of("message", "Expense deleted successfully"));
    }
}
