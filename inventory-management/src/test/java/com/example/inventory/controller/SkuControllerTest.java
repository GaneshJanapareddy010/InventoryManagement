package com.example.inventory.controller;

import com.example.inventory.dto.SkuDto;
import com.example.inventory.service.SkuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SkuController.class)
public class SkuControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SkuService skuService;

    @Autowired
    private ObjectMapper objectMapper;

    private SkuDto skuDto;

    @BeforeEach
    void setUp() {
        skuDto = new SkuDto();
        skuDto.setId(1L);
        skuDto.setCode("SKU123");
        skuDto.setQuantity(10);
        skuDto.setPrice(99.99);
        skuDto.setProductId(2L);
    }

    @Test
    void testAddSku() throws Exception {
        given(skuService.addSkuToProduct(eq(2L), any(SkuDto.class))).willReturn(skuDto);
        SkuController.SkuRequest request = new SkuController.SkuRequest();
        request.setCode("SKU123");
        request.setQuantity(10);
        request.setPrice(99.99);
        mockMvc.perform(post("/api/products/2/skus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.code").value("SKU123"));
    }

    @Test
    void testUpdateSku() throws Exception {
        given(skuService.updateSku(eq(1L), any(SkuDto.class))).willReturn(skuDto);
        SkuController.SkuRequest request = new SkuController.SkuRequest();
        request.setCode("SKU123");
        request.setQuantity(10);
        request.setPrice(99.99);
        mockMvc.perform(put("/api/products/2/skus/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.code").value("SKU123"));
    }

    @Test
    void testDeleteSku() throws Exception {
        doNothing().when(skuService).deleteSku(1L);
        mockMvc.perform(delete("/api/products/2/skus/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetSkusByProduct() throws Exception {
        List<SkuDto> skus = Arrays.asList(skuDto);
        given(skuService.getSkusByProduct(2L)).willReturn(skus);
        mockMvc.perform(get("/api/products/2/skus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].code").value("SKU123"));
    }
} 