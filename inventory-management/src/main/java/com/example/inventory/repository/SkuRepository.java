package com.example.inventory.repository;

import com.example.inventory.model.Sku;
import com.example.inventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SkuRepository extends JpaRepository<Sku, Long> {
    List<Sku> findByProduct(Product product);
    Optional<Sku> findByProductAndCode(Product product, String code);
} 