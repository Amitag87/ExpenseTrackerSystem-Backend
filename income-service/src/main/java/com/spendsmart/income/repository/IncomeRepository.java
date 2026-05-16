package com.spendsmart.income.repository;

import com.spendsmart.income.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<Income> findByUserId(Long userId);

    List<Income> findByUserIdAndSource(Long userId, String source);

    List<Income> findByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);

    @Query("SELECT i FROM Income i WHERE i.userId = :userId AND MONTH(i.date) = :month AND YEAR(i.date) = :year")
    List<Income> findByUserIdAndMonth(@Param("userId") Long userId,
                                     @Param("month") int month,
                                     @Param("year") int year);

    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.userId = :userId")
    Double sumAmountByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.userId = :userId AND MONTH(i.date) = :month AND YEAR(i.date) = :year")
    Double sumAmountByUserIdAndMonth(@Param("userId") Long userId,
                                    @Param("month") int month,
                                    @Param("year") int year);

    List<Income> findByIsRecurring(boolean isRecurring);

    // findByIncomeId - same as findById but explicit
    Optional<Income> findByIncomeId(Long incomeId);

    // deleteByIncomeId
    void deleteByIncomeId(Long incomeId);
}