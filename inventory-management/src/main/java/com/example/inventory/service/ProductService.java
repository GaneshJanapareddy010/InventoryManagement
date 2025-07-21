package com.example.inventory.service;

import com.example.inventory.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);
    ProductDto getProductById(Long id);
    ProductDto updateProduct(Long id, ProductDto productDto);
    void deleteProduct(Long id);
    Page<ProductDto> searchProducts(String name, Long categoryId, Pageable pageable);
} 