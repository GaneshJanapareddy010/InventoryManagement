package com.example.inventory.service.impl;

import com.example.inventory.dto.SkuDto;
import com.example.inventory.exception.CustomValidationException;
import com.example.inventory.model.Product;
import com.example.inventory.model.Sku;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.repository.SkuRepository;
import com.example.inventory.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    private SkuRepository skuRepository;
    @Autowired
    private ProductRepository productRepository;

    private void validateSkuFields(SkuDto skuDto) {
        if (skuDto.getPrice() <= 0) {
            throw new CustomValidationException("Price must be greater than 0");
        }
        if (skuDto.getQuantity() < 0) {
            throw new CustomValidationException("Quantity must be >= 0");
        }
    }

    private SkuDto toDto(Sku sku) {
        SkuDto dto = new SkuDto();
        dto.setId(sku.getId());
        dto.setCode(sku.getCode());
        dto.setQuantity(sku.getQuantity());
        dto.setProductId(sku.getProduct() != null ? sku.getProduct().getId() : null);
        // Add price if present in SkuDto/Sku
        try { dto.setPrice((Double) Sku.class.getDeclaredField("price").get(sku)); } catch (Exception ignored) {}
        return dto;
    }

    private Sku toEntity(SkuDto dto, Product product) {
        Sku sku = new Sku();
        sku.setId(dto.getId());
        sku.setCode(dto.getCode());
        sku.setQuantity(dto.getQuantity());
        sku.setProduct(product);
        // Add price if present in SkuDto/Sku
        try { Sku.class.getDeclaredField("price").set(sku, dto.getPrice()); } catch (Exception ignored) {}
        return sku;
    }

    @Override
    public SkuDto addSkuToProduct(Long productId, SkuDto skuDto) {
        validateSkuFields(skuDto);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        Optional<Sku> existing = skuRepository.findByProductAndCode(product, skuDto.getCode());
        if (existing.isPresent()) {
            throw new CustomValidationException("SKU with this code already exists for the product");
        }
        Sku sku = toEntity(skuDto, product);
        Sku saved = skuRepository.save(sku);
        return toDto(saved);
    }

    @Override
    public SkuDto updateSku(Long skuId, SkuDto skuDto) {
        validateSkuFields(skuDto);
        Sku sku = skuRepository.findById(skuId)
                .orElseThrow(() -> new EntityNotFoundException("SKU not found"));
        sku.setCode(skuDto.getCode());
        sku.setQuantity(skuDto.getQuantity());
        // Add price if present
        try { Sku.class.getDeclaredField("price").set(sku, skuDto.getPrice()); } catch (Exception ignored) {}
        Sku updated = skuRepository.save(sku);
        return toDto(updated);
    }

    @Override
    public void deleteSku(Long skuId) {
        if (!skuRepository.existsById(skuId)) {
            throw new EntityNotFoundException("SKU not found");
        }
        skuRepository.deleteById(skuId);
    }

    @Override
    public List<SkuDto> getSkusByProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        return skuRepository.findByProduct(product).stream().map(this::toDto).collect(Collectors.toList());
    }
} 