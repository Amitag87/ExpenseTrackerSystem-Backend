package com.spendsmart.budget.resource;


import com.spendsmart.budget.dto.BudgetProgress;
import com.spendsmart.budget.entity.Budget;
import com.spendsmart.budget.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetResource {

    private final BudgetService budgetService;

    // POST /budgets — Create budget
    @PostMapping
    public ResponseEntity<Budget> create(@RequestBody Budget budget) {
        return ResponseEntity.ok(budgetService.createBudget(budget));
    }

    // GET /budgets/{id} — Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<Budget> getById(@PathVariable Long id) {
        Optional<Budget> budget = budgetService.getBudgetById(id);
        return budget.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /budgets/user/{userId} — Get all by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Budget>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(budgetService.getBudgetsByUser(userId));
    }

    // GET /budgets/user/{userId}/active — Get active budgets
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<Budget>> getActive(@PathVariable Long userId) {
        return ResponseEntity.ok(budgetService.getActiveBudgets(userId));
    }

    // PUT /budgets/{id} — Update budget
    @PutMapping("/{id}")
    public ResponseEntity<Budget> update(@PathVariable Long id, @RequestBody Budget budget) {
        return ResponseEntity.ok(budgetService.updateBudget(id, budget));
    }

    // DELETE /budgets/{id} — Delete budget
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }

    // GET /budgets/{id}/progress — Get budget progress
    @GetMapping("/{id}/progress")
    public ResponseEntity<BudgetProgress> getProgress(@PathVariable Long id) {
        return ResponseEntity.ok(budgetService.getBudgetProgress(id));
    }

    // GET /budgets/user/{userId}/alerts — Check alerts
    @GetMapping("/user/{userId}/alerts")
    public ResponseEntity<List<String>> getAlerts(@PathVariable Long userId) {
        return ResponseEntity.ok(budgetService.checkBudgetAlerts(userId));
    }

    // POST /budgets/{id}/reset — Reset budget period
    @PostMapping("/{id}/reset")
    public ResponseEntity<Void> resetPeriod(@PathVariable Long id) {
        budgetService.resetBudgetPeriod(id);
        return ResponseEntity.ok().build();
    }

    // GET /budgets/user/{userId}/category/{categoryId}
    @GetMapping("/user/{userId}/category/{categoryId}")
    public ResponseEntity<Budget> getByCategory(
            @PathVariable Long userId,
            @PathVariable Long categoryId) {
        Optional<Budget> budget = budgetService.getBudgetsByCategory(userId, categoryId);
        return budget.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PATCH /budgets/{id}/spent — Update spent amount
    @PatchMapping("/{id}/spent")
    public ResponseEntity<Void> updateSpent(
            @PathVariable Long id,
            @RequestParam double amount) {
        budgetService.updateSpentAmount(id, amount);
        return ResponseEntity.ok().build();
    }

    // POST /budgets/{id}/spent — Update spent amount for inter-service clients
    @PostMapping("/{id}/spent")
    public ResponseEntity<Void> updateSpentPost(
            @PathVariable Long id,
            @RequestParam double amount) {
        budgetService.updateSpentAmount(id, amount);
        return ResponseEntity.ok().build();
    }
}
