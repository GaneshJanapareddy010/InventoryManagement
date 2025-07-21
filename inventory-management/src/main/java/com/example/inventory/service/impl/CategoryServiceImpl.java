package com.example.inventory.service.impl;

import com.example.inventory.dto.CategoryDto;
import com.example.inventory.model.Category;
import com.example.inventory.repository.CategoryRepository;
import com.example.inventory.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    private void validateCategoryName(String name) {
        if (!StringUtils.hasText(name) || name.length() < 4) {
            throw new IllegalArgumentException("Category name must not be null and must be at least 4 characters long.");
        }
    }

    private CategoryDto toDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        // Not including products for simplicity
        return dto;
    }

    private Category toEntity(CategoryDto dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        return category;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        validateCategoryName(categoryDto.getName());
        Category category = toEntity(categoryDto);
        Category saved = categoryRepository.save(category);
        return toDto(saved);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        return toDto(category);
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        validateCategoryName(categoryDto.getName());
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        category.setName(categoryDto.getName());
        Category updated = categoryRepository.save(category);
        return toDto(updated);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }
} 