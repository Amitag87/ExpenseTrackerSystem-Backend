package com.spendsmart.recurring.repository;


import com.spendsmart.recurring.entity.RecurringTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RecurringRepository extends JpaRepository<RecurringTransaction, Long> {

    List<RecurringTransaction> findByUserId(Long userId);

    List<RecurringTransaction> findByUserIdAndType(Long userId, String type);

    List<RecurringTransaction> findByUserIdAndIsActive(Long userId, boolean isActive);

    List<RecurringTransaction> findByNextDueDateBefore(LocalDate date);

    Optional<RecurringTransaction> findByRecurringId(Long id);

    List<RecurringTransaction> findByFrequency(String frequency);

    int countByUserIdAndIsActive(Long userId, boolean isActive);
}