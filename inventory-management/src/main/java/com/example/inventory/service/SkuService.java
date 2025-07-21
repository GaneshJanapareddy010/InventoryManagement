package com.example.inventory.service;

import com.example.inventory.dto.SkuDto;
import java.util.List;

public interface SkuService {
    SkuDto addSkuToProduct(Long productId, SkuDto skuDto);
    SkuDto updateSku(Long skuId, SkuDto skuDto);
    void deleteSku(Long skuId);
    List<SkuDto> getSkusByProduct(Long productId);
} 