package com.spendsmart.recurring.service;

import com.spendsmart.recurring.client.ExpenseClient;
import com.spendsmart.recurring.client.NotificationClient;
import com.spendsmart.recurring.entity.RecurringTransaction;
import com.spendsmart.recurring.repository.RecurringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.spendsmart.recurring.exception.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecurringServiceImpl implements RecurringService {

    private final RecurringRepository repo;
    private final ExpenseClient expenseClient;
    private final NotificationClient notificationClient;

    @Override
    public RecurringTransaction addRecurring(RecurringTransaction rt) {
        rt.setNextDueDate(rt.getStartDate());
        rt.setActive(true);
        return repo.save(rt);
    }

    @Override
    public List<RecurringTransaction> getByUser(Long userId) {
        return repo.findByUserId(userId);
    }

    @Override
    public Optional<RecurringTransaction> getById(Long id) {
        return repo.findById(id);
    }

    @Override
    public List<RecurringTransaction> getActiveRecurring(Long userId) {
        return repo.findByUserIdAndIsActive(userId, true);
    }

    @Override
    public RecurringTransaction updateRecurring(Long id, RecurringTransaction rt) {
        rt.setRecurringId(id);
        return repo.save(rt);
    }

    @Override
    public void deactivateRecurring(Long id) {
        RecurringTransaction rt = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));
        rt.setActive(false);
        repo.save(rt);
    }

    @Override
    public void deleteRecurring(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<RecurringTransaction> processUpcomingDue() {
        LocalDate today = LocalDate.now();

        List<RecurringTransaction> list =
                repo.findByNextDueDateBefore(today.plusDays(1));

        for (RecurringTransaction rt : list) {
            if (rt.isActive()) {
                generateTransactionFromRecurring(rt);
                notificationClient.sendReminder(
                        rt.getUserId(),
                        "Recurring Transaction Processed",
                        "Processed recurring " + rt.getType().name().toLowerCase() + " '" + rt.getTitle()
                                + "' for amount " + rt.getAmount() + ".",
                        rt.getRecurringId()
                );
                updateNextDueDate(rt.getRecurringId());
            }
        }
        return list;
    }

    @Override
    public void updateNextDueDate(Long id) {
        RecurringTransaction rt = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));

        LocalDate next = rt.getNextDueDate();

        switch (rt.getFrequency()) {
            case DAILY -> next = next.plusDays(1);
            case WEEKLY -> next = next.plusWeeks(1);
            case MONTHLY -> next = next.plusMonths(1);
            case QUARTERLY -> next = next.plusMonths(3);
            case YEARLY -> next = next.plusYears(1);
        }

        rt.setNextDueDate(next);
        repo.save(rt);
    }

    @Override
    public void generateTransactionFromRecurring(RecurringTransaction rt) {
        expenseClient.createTransaction(rt);
    }

    @Override
    public List<RecurringTransaction> getUpcomingThisMonth(Long userId) {
        LocalDate now = LocalDate.now();
        LocalDate end = now.withDayOfMonth(now.lengthOfMonth());

        return repo.findByUserId(userId).stream()
                .filter(rt -> rt.getNextDueDate().isBefore(end))
                .toList();
    }
}
