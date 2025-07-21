package com.example.inventory.controller;

import com.example.inventory.dto.SkuDto;
import com.example.inventory.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/products/{productId}/skus")
@Validated
public class SkuController {
    @Autowired
    private SkuService skuService;

    @Operation(summary = "Add SKU to product", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = SkuRequest.class))), responses = {
        @ApiResponse(responseCode = "201", description = "SKU created", content = @Content(schema = @Schema(implementation = SkuDto.class)))
    })
    @PostMapping
    public ResponseEntity<SkuDto> addSku(@PathVariable Long productId, @org.springframework.web.bind.annotation.RequestBody @Valid SkuRequest request) {
        SkuDto dto = new SkuDto();
        dto.setCode(request.getCode());
        dto.setQuantity(request.getQuantity());
        dto.setPrice(request.getPrice());
        SkuDto created = skuService.addSkuToProduct(productId, dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Update SKU", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = SkuRequest.class))), responses = {
        @ApiResponse(responseCode = "200", description = "SKU updated", content = @Content(schema = @Schema(implementation = SkuDto.class)))
    })
    @PutMapping("/{skuId}")
    public ResponseEntity<SkuDto> updateSku(@PathVariable Long productId, @PathVariable Long skuId, @org.springframework.web.bind.annotation.RequestBody @Valid SkuRequest request) {
        SkuDto dto = new SkuDto();
        dto.setCode(request.getCode());
        dto.setQuantity(request.getQuantity());
        dto.setPrice(request.getPrice());
        SkuDto updated = skuService.updateSku(skuId, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete SKU", responses = {
        @ApiResponse(responseCode = "204", description = "SKU deleted"),
        @ApiResponse(responseCode = "404", description = "SKU not found")
    })
    @DeleteMapping("/{skuId}")
    public ResponseEntity<Void> deleteSku(@PathVariable Long productId, @PathVariable Long skuId) {
        skuService.deleteSku(skuId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all SKUs for a product", responses = {
        @ApiResponse(responseCode = "200", description = "List of SKUs", content = @Content(schema = @Schema(implementation = SkuDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<SkuDto>> getSkusByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(skuService.getSkusByProduct(productId));
    }

    // Request DTO for validation
    public static class SkuRequest {
        @NotNull(message = "Code must not be null")
        private String code;
        @Min(value = 0, message = "Quantity must be >= 0")
        private int quantity;
        @NotNull(message = "Price must not be null")
        @Min(value = 1, message = "Price must be > 0")
        private Double price;
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
    }
} 