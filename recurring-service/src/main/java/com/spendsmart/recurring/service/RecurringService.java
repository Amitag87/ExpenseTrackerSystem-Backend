package com.spendsmart.recurring.service;


import com.spendsmart.recurring.entity.RecurringTransaction;

import java.util.List;
import java.util.Optional;

public interface RecurringService {

    RecurringTransaction addRecurring(RecurringTransaction rt);

    List<RecurringTransaction> getByUser(Long userId);

    Optional<RecurringTransaction> getById(Long id);

    List<RecurringTransaction> getActiveRecurring(Long userId);

    RecurringTransaction updateRecurring(Long id, RecurringTransaction rt);

    void deactivateRecurring(Long id);

    void deleteRecurring(Long id);

    List<RecurringTransaction> processUpcomingDue();

    void updateNextDueDate(Long id);

    void generateTransactionFromRecurring(RecurringTransaction rt);

    List<RecurringTransaction> getUpcomingThisMonth(Long userId);
}