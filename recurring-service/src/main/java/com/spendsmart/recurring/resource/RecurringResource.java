package com.spendsmart.recurring.resource;

import com.spendsmart.recurring.dto.RecurringMapper;
import com.spendsmart.recurring.dto.RecurringRequest;
import com.spendsmart.recurring.dto.RecurringResponse;
import com.spendsmart.recurring.entity.RecurringTransaction;
import com.spendsmart.recurring.exception.ResourceNotFoundException;
import com.spendsmart.recurring.service.RecurringService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing recurring transactions.
 * Exposes endpoints to create, read, update, and delete recurring transactions for users.
 */
@RestController
@RequestMapping("/api/recurring")
@RequiredArgsConstructor
public class RecurringResource {

    private final RecurringService service;
    private final RecurringMapper mapper;

    /**
     * Adds a new recurring transaction.
     *
     * @param request the request payload containing details of the recurring transaction
     * @return a response containing the saved transaction details
     */
    @PostMapping
    public ResponseEntity<RecurringResponse> add(@Valid @RequestBody RecurringRequest request) {
        RecurringTransaction rt = mapper.toEntity(request);
        RecurringTransaction saved = service.addRecurring(rt);
        return ResponseEntity.ok(mapper.toResponse(saved));
    }

    /**
     * Retrieves all recurring transactions for a specific user.
     *
     * @param id the user ID
     * @return a list of recurring transactions belonging to the user
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<List<RecurringResponse>> getByUser(@PathVariable Long id) {
        List<RecurringTransaction> transactions = service.getByUser(id);
        return ResponseEntity.ok(transactions.stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    /**
     * Retrieves a recurring transaction by its unique ID.
     *
     * @param id the recurring transaction ID
     * @return the requested recurring transaction
     * @throws ResourceNotFoundException if the transaction is not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecurringResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(service.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"))));
    }

    /**
     * Retrieves all active recurring transactions for a specific user.
     *
     * @param userId the user ID
     * @return a list of active recurring transactions belonging to the user
     */
    @GetMapping("/active/{userId}")
    public ResponseEntity<List<RecurringResponse>> active(@PathVariable Long userId) {
        List<RecurringTransaction> transactions = service.getActiveRecurring(userId);
        return ResponseEntity.ok(transactions.stream().map(mapper::toResponse).collect(Collectors.toList()));
    }

    /**
     * Updates an existing recurring transaction.
     *
     * @param id      the ID of the recurring transaction to update
     * @param request the new details for the recurring transaction
     * @return the updated recurring transaction
     */
    @PutMapping("/{id}")
    public ResponseEntity<RecurringResponse> update(@PathVariable Long id, @Valid @RequestBody RecurringRequest request) {
        RecurringTransaction rt = mapper.toEntity(request);
        RecurringTransaction updated = service.updateRecurring(id, rt);
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    /**
     * Deletes a recurring transaction by its ID.
     *
     * @param id the ID of the recurring transaction to delete
     * @return a success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteRecurring(id);
        return ResponseEntity.ok("Deleted");
    }
}