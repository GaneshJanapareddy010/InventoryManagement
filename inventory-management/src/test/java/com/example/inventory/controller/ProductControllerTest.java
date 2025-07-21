package com.example.inventory.controller;

import com.example.inventory.dto.ProductDto;
import com.example.inventory.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("iPhone");
        productDto.setCategoryId(2L);
    }

    @Test
    void testCreateProduct() throws Exception {
        given(productService.createProduct(any(ProductDto.class))).willReturn(productDto);
        ProductController.ProductRequest request = new ProductController.ProductRequest();
        request.setName("iPhone");
        request.setCategoryId(2L);
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("iPhone"));
    }

    @Test
    void testGetProductById() throws Exception {
        given(productService.getProductById(1L)).willReturn(productDto);
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("iPhone"));
    }

    @Test
    void testUpdateProduct() throws Exception {
        given(productService.updateProduct(eq(1L), any(ProductDto.class))).willReturn(productDto);
        ProductController.ProductRequest request = new ProductController.ProductRequest();
        request.setName("iPhone");
        request.setCategoryId(2L);
        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("iPhone"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testSearchProducts() throws Exception {
        List<ProductDto> products = Arrays.asList(productDto);
        Page<ProductDto> page = new PageImpl<>(products, PageRequest.of(0, 10), 1);
        given(productService.searchProducts(anyString(), any(), any(Pageable.class))).willReturn(page);
        mockMvc.perform(get("/api/products?name=iphone&categoryId=2&page=0&pageSize=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("iPhone"));
    }
} 