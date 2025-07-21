package com.example.inventory.dto;

import lombok.Data;

@Data
public class SkuDto {
    private Long id;
    private String code;
    private int quantity;
    private Long productId;
    private Double price;

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
} 