package com.spendsmart.category.resource;

import com.spendsmart.category.dto.CategoryMapper;
import com.spendsmart.category.dto.CategoryRequest;
import com.spendsmart.category.dto.CategoryResponse;
import com.spendsmart.category.entity.Category;
import com.spendsmart.category.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller exposing category management endpoints under /categories.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryMapper categoryMapper;

    // ─── Endpoints ───────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        Category created = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.toResponse(created));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CategoryResponse>> getByUser(@PathVariable Long userId) {
        List<Category> categories = categoryService.getByUserId(userId);
        return ResponseEntity.ok(categories.stream().map(categoryMapper::toResponse).collect(Collectors.toList()));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long categoryId) {
        return categoryService.getCategoryById(categoryId)
                .map(category -> ResponseEntity.ok(categoryMapper.toResponse(category)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}/type/{type}")
    public ResponseEntity<List<CategoryResponse>> getByType(
            @PathVariable Long userId,
            @PathVariable String type) {
        List<Category> categories = categoryService.getByUserAndType(userId, type);
        return ResponseEntity.ok(categories.stream().map(categoryMapper::toResponse).collect(Collectors.toList()));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        Category updated = categoryService.updateCategory(categoryId, category);
        return ResponseEntity.ok(categoryMapper.toResponse(updated));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> delete(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/defaults")
    public ResponseEntity<List<CategoryResponse>> getDefaults() {
        List<Category> defaults = categoryService.getDefaultCategories();
        return ResponseEntity.ok(defaults.stream().map(categoryMapper::toResponse).collect(Collectors.toList()));
    }

    @PostMapping("/user/{userId}/defaults")
    public ResponseEntity<Void> initDefaults(@PathVariable Long userId) {
        categoryService.initDefaultCategories(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{categoryId}/budget")
    public ResponseEntity<Void> setBudget(
            @PathVariable Long categoryId,
            @RequestParam double amount) {
        categoryService.setCategoryBudget(categoryId, amount);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> getCount(@PathVariable Long userId) {
        Long count = categoryService.getCategoryCount(userId);
        return ResponseEntity.ok(count);
    }
}
