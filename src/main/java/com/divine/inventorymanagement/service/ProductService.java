package com.divine.inventorymanagement.service;

import com.divine.inventorymanagement.dto.*;
import com.divine.inventorymanagement.exception.DuplicateResourceException;
import com.divine.inventorymanagement.exception.ResourceNotFoundException;
import com.divine.inventorymanagement.model.Customer;
import com.divine.inventorymanagement.model.Product;
import com.divine.inventorymanagement.repository.CustomerRepository;
import com.divine.inventorymanagement.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        productRepository.findBySku(request.sku()).ifPresent(product -> {
            throw new DuplicateResourceException("Product with SKU " + request.sku() + " already exists");
        });

        Product product = Product.builder()
                .sku(request.sku().trim())
                .name(request.name().trim())
                .description(request.description())
                .price(request.price())
                .stockQuantity(request.stockQuantity())
                .category(request.category().trim())
                .active(request.active() == null || request.active())
                .build();

        Product saved = productRepository.save(product);
        return mapToResponse(saved);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public List<ProductResponse> searchProducts(String name, String category) {
        if (name != null && !name.isBlank() && category != null && !category.isBlank()) {
            return productRepository.findAll().stream()
                    .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase())
                            && product.getCategory().equalsIgnoreCase(category))
                    .map(this::mapToResponse)
                    .toList();
        }
        if (name != null && !name.isBlank()) {
            return productRepository.findByNameContainingIgnoreCase(name).stream().map(this::mapToResponse).toList();
        }
        if (category != null && !category.isBlank()) {
            return productRepository.findByCategoryIgnoreCase(category).stream().map(this::mapToResponse).toList();
        }
        return getAllProducts();
    }

    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToResponse(product);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        productRepository.findBySku(request.sku()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new DuplicateResourceException("Product with SKU " + request.sku() + " already exists");
            }
        });

        product.setSku(request.sku().trim());
        product.setName(request.name().trim());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setCategory(request.category().trim());
        product.setActive(request.active() == null || request.active());

        return mapToResponse(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    public List<LowStockProductResponse> getLowStockProducts(Integer threshold) {
        int stockThreshold = threshold == null ? 5 : threshold;
        return productRepository.findByStockQuantityLessThan(stockThreshold).stream()
                .map(product -> new LowStockProductResponse(
                        product.getId(),
                        product.getSku(),
                        product.getName(),
                        product.getStockQuantity(),
                        product.getCategory()))
                .toList();
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory(),
                product.isActive(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
