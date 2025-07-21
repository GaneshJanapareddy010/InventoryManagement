package com.example.inventory.service.impl;

import com.example.inventory.dto.ProductDto;
import com.example.inventory.exception.CustomValidationException;
import com.example.inventory.model.Category;
import com.example.inventory.model.Product;
import com.example.inventory.repository.CategoryRepository;
import com.example.inventory.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category();
        category.setId(2L);
        category.setName("Electronics");
        product = new Product();
        product.setId(1L);
        product.setName("iPhone");
        product.setCategory(category);
    }

    @Test
    void testCreateProduct() {
        ProductDto dto = new ProductDto();
        dto.setName("iPhone");
        dto.setCategoryId(2L);
        given(categoryRepository.findById(2L)).willReturn(Optional.of(category));
        given(productRepository.save(any(Product.class))).willReturn(product);
        ProductDto result = productService.createProduct(dto);
        assertEquals("iPhone", result.getName());
        assertEquals(2L, result.getCategoryId());
    }

    @Test
    void testCreateProduct_CategoryNotFound() {
        ProductDto dto = new ProductDto();
        dto.setName("iPhone");
        dto.setCategoryId(99L);
        given(categoryRepository.findById(99L)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> productService.createProduct(dto));
    }

    @Test
    void testCreateProduct_NullCategory() {
        ProductDto dto = new ProductDto();
        dto.setName("iPhone");
        dto.setCategoryId(null);
        assertThrows(CustomValidationException.class, () -> productService.createProduct(dto));
    }

    @Test
    void testGetProductById() {
        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        ProductDto result = productService.getProductById(1L);
        assertEquals("iPhone", result.getName());
    }

    @Test
    void testGetProductById_NotFound() {
        given(productRepository.findById(99L)).willReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> productService.getProductById(99L));
    }

    @Test
    void testUpdateProduct() {
        ProductDto dto = new ProductDto();
        dto.setName("iPhone 13");
        dto.setCategoryId(2L);
        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        given(categoryRepository.findById(2L)).willReturn(Optional.of(category));
        given(productRepository.save(any(Product.class))).willReturn(product);
        ProductDto result = productService.updateProduct(1L, dto);
        assertEquals("iPhone 13", result.getName());
    }

    @Test
    void testDeleteProduct() {
        given(productRepository.existsById(1L)).willReturn(true);
        doNothing().when(productRepository).deleteById(1L);
        assertDoesNotThrow(() -> productService.deleteProduct(1L));
    }

    @Test
    void testDeleteProduct_NotFound() {
        given(productRepository.existsById(99L)).willReturn(false);
        assertThrows(EntityNotFoundException.class, () -> productService.deleteProduct(99L));
    }

    @Test
    void testSearchProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(Arrays.asList(product), pageable, 1);
        given(productRepository.search(anyString(), any(), any(Pageable.class))).willReturn(page);
        Page<ProductDto> result = productService.searchProducts("iPhone", 2L, pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals("iPhone", result.getContent().get(0).getName());
    }
} 