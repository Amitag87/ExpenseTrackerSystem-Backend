package com.spendsmart.income.resource;

import com.spendsmart.income.dto.IncomeMapper;
import com.spendsmart.income.dto.IncomeRequest;
import com.spendsmart.income.dto.IncomeResponse;
import com.spendsmart.income.entity.Income;
import com.spendsmart.income.service.IncomeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/incomes")
public class IncomeResource {

    @Autowired
    private IncomeService service;

    @Autowired
    private IncomeMapper mapper;

    // ✅ POST - Add Income
    @PostMapping
    public ResponseEntity<IncomeResponse> add(@Valid @RequestBody IncomeRequest request) {
        Income income = mapper.toEntity(request);
        Income savedIncome = service.addIncome(income);
        return ResponseEntity.ok(mapper.toResponse(savedIncome));
    }

    // ✅ GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<IncomeResponse> getById(@PathVariable Long id) {
        Income income = service.getIncomeById(id);
        return ResponseEntity.ok(mapper.toResponse(income));
    }

    // ✅ GET by User
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<IncomeResponse>> getByUser(@PathVariable Long userId) {
        List<Income> incomes = service.getIncomesByUser(userId);
        return ResponseEntity.ok(incomes.stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    // ✅ GET by Source
    @GetMapping("/source")
    public ResponseEntity<List<IncomeResponse>> getBySource(
            @RequestParam Long userId,
            @RequestParam String source) {
        List<Income> incomes = service.getIncomesBySource(userId, source);
        return ResponseEntity.ok(incomes.stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    // ✅ GET by Date Range
    @GetMapping("/range")
    public ResponseEntity<List<IncomeResponse>> getByDateRange(
            @RequestParam Long userId,
            @RequestParam String start,
            @RequestParam String end) {
        List<Income> incomes = service.getIncomesByDateRange(
                userId,
                LocalDate.parse(start),
                LocalDate.parse(end));
        return ResponseEntity.ok(incomes.stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    // ✅ GET by Month
    @GetMapping("/month")
    public ResponseEntity<List<IncomeResponse>> getByMonth(
            @RequestParam Long userId,
            @RequestParam int month,
            @RequestParam int year) {
        List<Income> incomes = service.getIncomesByMonth(userId, month, year);
        return ResponseEntity.ok(incomes.stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    // ✅ GET Recurring Incomes
    @GetMapping("/recurring")
    public ResponseEntity<List<IncomeResponse>> getRecurring() {
        List<Income> incomes = service.getRecurringIncomes();
        return ResponseEntity.ok(incomes.stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    // ✅ GET Total by User
    @GetMapping("/user/{userId}/total")
    public ResponseEntity<Double> getTotalByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getTotalIncomeByUser(userId));
    }

    // ✅ GET Total by Month
    @GetMapping("/total/month")
    public ResponseEntity<Double> getTotalByMonth(
            @RequestParam Long userId,
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(service.getTotalIncomeByMonth(userId, month, year));
    }

    // ✅ PUT - Update Income
    @PutMapping("/{id}")
    public ResponseEntity<IncomeResponse> update(@PathVariable Long id, @Valid @RequestBody IncomeRequest request) {
        Income income = mapper.toEntity(request);
        Income updatedIncome = service.updateIncome(id, income);
        return ResponseEntity.ok(mapper.toResponse(updatedIncome));
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        service.deleteIncome(id);
        return ResponseEntity.ok(Map.of("message", "Income deleted successfully with id: " + id));
    }
}
