package com.example.inventory.service.impl;

import com.example.inventory.dto.SkuDto;
import com.example.inventory.exception.CustomValidationException;
import com.example.inventory.model.Product;
import com.example.inventory.model.Sku;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.repository.SkuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class SkuServiceImplTest {
    @Mock
    private SkuRepository skuRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private SkuServiceImpl skuService;

    private Product product;
    private Sku sku;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setId(2L);
        product.setName("Test Product");
        sku = new Sku();
        sku.setId(1L);
        sku.setCode("SKU123");
        sku.setQuantity(10);
        sku.setPrice(99.99);
        sku.setProduct(product);
    }

    @Test
    void testAddSkuToProduct() {
        SkuDto dto = new SkuDto();
        dto.setCode("SKU123");
        dto.setQuantity(10);
        dto.setPrice(99.99);
        given(productRepository.findById(2L)).willReturn(Optional.of(product));
        given(skuRepository.findByProductAndCode(product, "SKU123")).willReturn(Optional.empty());
        given(skuRepository.save(any(Sku.class))).willReturn(sku);
        SkuDto result = skuService.addSkuToProduct(2L, dto);
        assertEquals("SKU123", result.getCode());
        assertEquals(99.99, result.getPrice());
    }

    @Test
    void testAddSkuToProduct_DuplicateCode() {
        SkuDto dto = new SkuDto();
        dto.setCode("SKU123");
        dto.setQuantity(10);
        dto.setPrice(99.99);
        given(productRepository.findById(2L)).willReturn(Optional.of(product));
        given(skuRepository.findByProductAndCode(product, "SKU123")).willReturn(Optional.of(sku));
        assertThrows(CustomValidationException.class, () -> skuService.addSkuToProduct(2L, dto));
    }

    @Test
    void testAddSkuToProduct_InvalidPrice() {
        SkuDto dto = new SkuDto();
        dto.setCode("SKU123");
        dto.setQuantity(10);
        dto.setPrice(0.0);
        given(productRepository.findById(2L)).willReturn(Optional.of(product));
        assertThrows(CustomValidationException.class, () -> skuService.addSkuToProduct(2L, dto));
    }

    @Test
    void testUpdateSku() {
        SkuDto dto = new SkuDto();
        dto.setCode("SKU123");
        dto.setQuantity(20);
        dto.setPrice(199.99);
        given(skuRepository.findById(1L)).willReturn(Optional.of(sku));
        given(skuRepository.save(any(Sku.class))).willReturn(sku);
        SkuDto result = skuService.updateSku(1L, dto);
        assertEquals(20, result.getQuantity());
        assertEquals(199.99, result.getPrice());
    }
} 