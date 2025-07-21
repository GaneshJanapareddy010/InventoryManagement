package com.example.inventory.service;

import com.example.inventory.dto.CategoryDto;
import com.example.inventory.model.Category;
import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto getCategoryById(Long id);
    CategoryDto updateCategory(Long id, CategoryDto categoryDto);
    void deleteCategory(Long id);
    List<CategoryDto> getAllCategories();
} 