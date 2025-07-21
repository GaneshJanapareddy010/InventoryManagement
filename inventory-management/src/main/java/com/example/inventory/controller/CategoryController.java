package com.example.inventory.controller;

import com.example.inventory.dto.CategoryDto;
import com.example.inventory.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/categories")
@Validated
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Create a new category", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CategoryRequest.class))), responses = {
        @ApiResponse(responseCode = "201", description = "Category created", content = @Content(schema = @Schema(implementation = CategoryDto.class)))
    })
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@org.springframework.web.bind.annotation.RequestBody @Valid CategoryRequest request) {
        CategoryDto dto = new CategoryDto();
        dto.setName(request.getName());
        CategoryDto created = categoryService.createCategory(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Get category by ID", responses = {
        @ApiResponse(responseCode = "200", description = "Category found", content = @Content(schema = @Schema(implementation = CategoryDto.class))),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @Operation(summary = "Update category", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CategoryRequest.class))), responses = {
        @ApiResponse(responseCode = "200", description = "Category updated", content = @Content(schema = @Schema(implementation = CategoryDto.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @org.springframework.web.bind.annotation.RequestBody @Valid CategoryRequest request) {
        CategoryDto dto = new CategoryDto();
        dto.setName(request.getName());
        CategoryDto updated = categoryService.updateCategory(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete category", responses = {
        @ApiResponse(responseCode = "204", description = "Category deleted"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List all categories", responses = {
        @ApiResponse(responseCode = "200", description = "List of categories", content = @Content(schema = @Schema(implementation = CategoryDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // Request DTO for validation
    public static class CategoryRequest {
        @NotNull(message = "Name must not be null")
        @Size(min = 4, message = "Name must be at least 4 characters long")
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
} 