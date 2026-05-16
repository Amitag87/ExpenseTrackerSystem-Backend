package com.spendsmart.category.service;

import com.spendsmart.category.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    /**
     * Create a new category.
     *
     * @param category the category to persist
     * @return the saved Category with generated ID
     */
    Category createCategory(Category category);

    /**
     * Get all categories belonging to a user.
     *
     * @param userId the user's ID
     * @return list of categories owned by the user
     */
    List<Category> getByUserId(Long userId);

    /**
     * Fetch a single category by its ID.
     *
     * @param categoryId the category's ID
     * @return Optional containing the category, or empty if not found
     */
    Optional<Category> getCategoryById(Long categoryId);

    /**
     * Fetch categories for a user filtered by transaction type.
     *
     * @param userId the user's ID
     * @param type   EXPENSE or INCOME
     * @return list of matching categories
     */
    List<Category> getByUserAndType(Long userId, String type);

    /**
     * Update an existing category.
     *
     * @param categoryId the ID of the category to update
     * @param category   object carrying new values
     * @return the updated Category
     */
    Category updateCategory(Long categoryId, Category category);

    /**
     * Delete a category by its ID.
     *
     * @param categoryId the category's ID
     */
    void deleteCategory(Long categoryId);

    /**
     * Get all system-seeded default categories.
     *
     * @return list of default categories
     */
    List<Category> getDefaultCategories();

    /**
     * Seed default system categories for a newly registered user.
     * Called automatically on user registration.
     *
     * @param userId the new user's ID
     */
    void initDefaultCategories(Long userId);

    /**
     * Set (or update) a budget limit on a specific category.
     *
     * @param categoryId  the category's ID
     * @param budgetLimit the new budget limit amount
     */
    void setCategoryBudget(Long categoryId, double budgetLimit);

    /**
     * Return the total number of categories for a user.
     *
     * @param userId the user's ID
     * @return category count
     */
    Long getCategoryCount(Long userId);
}