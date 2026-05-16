package com.spendsmart.category.repository;

import com.spendsmart.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Find all categories belonging to a user.
     */
    List<Category> findByUserId(Long userId);

    /**
     * Find categories for a user filtered by type (EXPENSE or INCOME).
     */
    List<Category> findByUserIdAndType(Long userId, String type);

    /**
     * Find a single category by its ID.
     */
    Optional<Category> findByCategoryId(Long categoryId);

    /**
     * Find a category by user ID and name (for duplicate-name validation).
     */
    Optional<Category> findByUserIdAndName(Long userId, String name);

    /**
     * Find all system-seeded default categories.
     */
    List<Category> findByIsDefault(boolean isDefault);

    /**
     * Count how many categories a user has.
     */
    Long countByUserId(Long userId);

    /**
     * Delete all categories owned by a user (used when user account is removed).
     */
    void deleteByUserId(Long userId);

    /**
     * Delete a specific category by its ID.
     */
    void deleteByCategoryId(Long categoryId);
}