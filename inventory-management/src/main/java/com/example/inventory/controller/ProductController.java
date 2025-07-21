package com.example.inventory.controller;

import com.example.inventory.dto.ProductDto;
import com.example.inventory.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {
    @Autowired
    private ProductService productService;

    @Operation(summary = "Create a new product", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = ProductRequest.class))), responses = {
        @ApiResponse(responseCode = "201", description = "Product created", content = @Content(schema = @Schema(implementation = ProductDto.class)))
    })
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@org.springframework.web.bind.annotation.RequestBody @Valid ProductRequest request) {
        ProductDto dto = new ProductDto();
        dto.setName(request.getName());
        dto.setCategoryId(request.getCategoryId());
        ProductDto created = productService.createProduct(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Get product by ID", responses = {
        @ApiResponse(responseCode = "200", description = "Product found", content = @Content(schema = @Schema(implementation = ProductDto.class))),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Update product", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = ProductRequest.class))), responses = {
        @ApiResponse(responseCode = "200", description = "Product updated", content = @Content(schema = @Schema(implementation = ProductDto.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @org.springframework.web.bind.annotation.RequestBody @Valid ProductRequest request) {
        ProductDto dto = new ProductDto();
        dto.setName(request.getName());
        dto.setCategoryId(request.getCategoryId());
        ProductDto updated = productService.updateProduct(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete product", responses = {
        @ApiResponse(responseCode = "204", description = "Product deleted"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search and filter products", responses = {
        @ApiResponse(responseCode = "200", description = "List of products", content = @Content(schema = @Schema(implementation = ProductDto.class)))
    })
    @GetMapping
    public ResponseEntity<Page<ProductDto>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<ProductDto> result = productService.searchProducts(name, categoryId, pageable);
        return ResponseEntity.ok(result);
    }

    // Request DTO for validation
    public static class ProductRequest {
        @NotNull(message = "Name must not be null")
        @Size(min = 2, message = "Name must be at least 2 characters long")
        private String name;
        @NotNull(message = "CategoryId must not be null")
        private Long categoryId;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Long getCategoryId() { return categoryId; }
        public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    }
} 