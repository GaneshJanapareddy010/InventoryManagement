package com.example.inventory.service.impl;

import com.example.inventory.dto.CategoryDto;
import com.example.inventory.exception.CustomValidationException;
import com.example.inventory.model.Category;
import com.example.inventory.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");
    }

    @Test
    void testCreateCategory() {
        CategoryDto dto = new CategoryDto();
        dto.setName("Electronics");
        given(categoryRepository.save(any(Category.class))).willReturn(category);
        CategoryDto result = categoryService.createCategory(dto);
        assertEquals("Electronics", result.getName());
    }

    @Test
    void testCreateCategory_InvalidName() {
        CategoryDto dto = new CategoryDto();
        dto.setName("abc");
        assertThrows(CustomValidationException.class, () -> categoryService.createCategory(dto));
    }

    @Test
    void testGetCategoryById() {
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        CategoryDto result = categoryService.getCategoryById(1L);
        assertEquals("Electronics", result.getName());
    }

    @Test
    void testGetCategoryById_NotFound() {
        given(categoryRepository.findById(99L)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> categoryService.getCategoryById(99L));
    }

    @Test
    void testUpdateCategory() {
        CategoryDto dto = new CategoryDto();
        dto.setName("Home Appliances");
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        given(categoryRepository.save(any(Category.class))).willReturn(category);
        CategoryDto result = categoryService.updateCategory(1L, dto);
        assertEquals("Home Appliances", result.getName());
    }

    @Test
    void testUpdateCategory_InvalidName() {
        CategoryDto dto = new CategoryDto();
        dto.setName("abc");
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        assertThrows(CustomValidationException.class, () -> categoryService.updateCategory(1L, dto));
    }

    @Test
    void testUpdateCategory_NotFound() {
        CategoryDto dto = new CategoryDto();
        dto.setName("Electronics");
        given(categoryRepository.findById(99L)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> categoryService.updateCategory(99L, dto));
    }

    @Test
    void testDeleteCategory() {
        given(categoryRepository.existsById(1L)).willReturn(true);
        doNothing().when(categoryRepository).deleteById(1L);
        assertDoesNotThrow(() -> categoryService.deleteCategory(1L));
    }

    @Test
    void testDeleteCategory_NotFound() {
        given(categoryRepository.existsById(99L)).willReturn(false);
        assertThrows(EntityNotFoundException.class, () -> categoryService.deleteCategory(99L));
    }

    @Test
    void testGetAllCategories() {
        given(categoryRepository.findAll()).willReturn(Arrays.asList(category));
        List<CategoryDto> result = categoryService.getAllCategories();
        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getName());
    }
} 