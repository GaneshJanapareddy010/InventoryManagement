package com.example.inventory.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private Long categoryId;
    private List<SkuDto> skus;
} 