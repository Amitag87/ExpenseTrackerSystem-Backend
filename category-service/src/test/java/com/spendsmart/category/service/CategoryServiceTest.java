package com.spendsmart.category.service;

import com.spendsmart.category.entity.Category;
import com.spendsmart.category.repository.CategoryRepository;
import com.spendsmart.category.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository catRepo;

    @InjectMocks
    private CategoryServiceImpl service;

    private Category sampleCategory;

    @BeforeEach
    void setUp() {
        sampleCategory = new Category();
        sampleCategory.setCategoryId(1L);
        sampleCategory.setUserId(100L);
        sampleCategory.setName("Food");
        sampleCategory.setType("EXPENSE");
    }

    @Test
    void createCategory_Success() {
        when(catRepo.save(any(Category.class))).thenReturn(sampleCategory);
        
        Category result = service.createCategory(sampleCategory);
        
        assertNotNull(result);
        assertEquals("Food", result.getName());
        verify(catRepo, times(1)).save(sampleCategory);
    }

    @Test
    void initDefaultCategories_Success() {
        // Just verify it doesn't crash and calls save multiple times
        service.initDefaultCategories(100L);
        
        verify(catRepo, atLeast(5)).save(any(Category.class));
    }

    @Test
    void updateCategory_NotFound() {
        when(catRepo.findByCategoryId(1L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> service.updateCategory(1L, sampleCategory));
    }
}
