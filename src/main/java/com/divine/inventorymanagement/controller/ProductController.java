package com.divine.inventorymanagement.controller;

import com.divine.inventorymanagement.dto.*;
import com.divine.inventorymanagement.model.OrderStatus;
import com.divine.inventorymanagement.service.CustomerService;
import com.divine.inventorymanagement.service.OrderService;
import com.divine.inventorymanagement.service.ProductService;
import com.divine.inventorymanagement.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        return new ResponseEntity<>(productService.createProduct(request), HttpStatus.CREATED);
    }

    @GetMapping("/products")
    public List<ProductResponse> getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category) {
        return productService.searchProducts(name, category);
    }

    @GetMapping("/products/{id}")
    public ProductResponse getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @PutMapping("/products/{id}")
    public ProductResponse updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
