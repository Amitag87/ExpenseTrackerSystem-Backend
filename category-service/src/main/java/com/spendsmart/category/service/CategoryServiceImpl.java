package com.spendsmart.category.service;

import com.spendsmart.category.entity.Category;
import com.spendsmart.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.spendsmart.category.exception.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository catRepo;

    // ─── Default category seeds ──────────────────────────────────────────────

    /** Default EXPENSE categories seeded for every new user. */
    private static final List<Object[]> DEFAULT_EXPENSES = Arrays.asList(
            new Object[]{"Food",          "🍔", "#FF6B6B"},
            new Object[]{"Transport",     "🚌", "#4ECDC4"},
            new Object[]{"Bills",         "🧾", "#45B7D1"},
            new Object[]{"Health",        "💊", "#96CEB4"},
            new Object[]{"Entertainment", "🎬", "#FFEAA7"},
            new Object[]{"Shopping",      "🛍️", "#DDA0DD"},
            new Object[]{"Education",     "📚", "#98D8C8"}
    );

    /** Default INCOME categories seeded for every new user. */
    private static final List<Object[]> DEFAULT_INCOMES = Arrays.asList(
            new Object[]{"Salary",     "💼", "#2ECC71"},
            new Object[]{"Freelance",  "💻", "#3498DB"},
            new Object[]{"Investment", "📈", "#9B59B6"},
            new Object[]{"Gift",       "🎁", "#E91E63"}
    );

    // ─── Constructor injection ───────────────────────────────────────────────

    @Autowired
    public CategoryServiceImpl(CategoryRepository catRepo) {
        this.catRepo = catRepo;
    }

    // ─── CategoryService implementation ─────────────────────────────────────

    @Override
    public Category createCategory(Category category) {
        if (category.getCreatedAt() == null) {
            category.setCreatedAt(LocalDate.now());
        }
        return catRepo.save(category);
    }

    @Override
    public List<Category> getByUserId(Long userId) {
        return catRepo.findByUserId(userId);
    }

    @Override
    public Optional<Category> getCategoryById(Long categoryId) {
        return catRepo.findByCategoryId(categoryId);
    }

    @Override
    public List<Category> getByUserAndType(Long userId, String type) {
        return catRepo.findByUserIdAndType(userId, type);
    }

    @Override
    @Transactional
    public Category updateCategory(Long categoryId, Category updated) {
        Category existing = catRepo.findByCategoryId(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + categoryId));

        existing.setName(updated.getName());
        existing.setType(updated.getType());
        existing.setIcon(updated.getIcon());
        existing.setColorCode(updated.getColorCode());
        existing.setBudgetLimit(updated.getBudgetLimit());

        return catRepo.save(existing);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        catRepo.deleteByCategoryId(categoryId);
    }

    @Override
    public List<Category> getDefaultCategories() {
        return catRepo.findByIsDefault(true);
    }

    @Override
    @Transactional
    public void initDefaultCategories(Long userId) {
        // Seed EXPENSE defaults
        for (Object[] seed : DEFAULT_EXPENSES) {
            Category c = new Category();
            c.setUserId(userId);
            c.setName((String) seed[0]);
            c.setType("EXPENSE");
            c.setIcon((String) seed[1]);
            c.setColorCode((String) seed[2]);
            c.setBudgetLimit(0.0);
            c.setDefault(true);
            c.setCreatedAt(LocalDate.now());
            catRepo.save(c);
        }

        // Seed INCOME defaults
        for (Object[] seed : DEFAULT_INCOMES) {
            Category c = new Category();
            c.setUserId(userId);
            c.setName((String) seed[0]);
            c.setType("INCOME");
            c.setIcon((String) seed[1]);
            c.setColorCode((String) seed[2]);
            c.setBudgetLimit(0.0);
            c.setDefault(true);
            c.setCreatedAt(LocalDate.now());
            catRepo.save(c);
        }
    }

    @Override
    @Transactional
    public void setCategoryBudget(Long categoryId, double budgetLimit) {
        Category category = catRepo.findByCategoryId(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + categoryId));
        category.setBudgetLimit(budgetLimit);
        catRepo.save(category);
    }

    @Override
    public Long getCategoryCount(Long userId) {
        return catRepo.countByUserId(userId);
    }
}