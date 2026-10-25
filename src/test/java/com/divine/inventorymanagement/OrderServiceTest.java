package com.divine.inventorymanagement;

import com.divine.inventorymanagement.dto.*;
import com.divine.inventorymanagement.model.Customer;
import com.divine.inventorymanagement.model.Product;
import com.divine.inventorymanagement.repository.CustomerRepository;
import com.divine.inventorymanagement.repository.ProductRepository;
import com.divine.inventorymanagement.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    private Customer customer;
    private Product product;

    @BeforeEach
    void setUp() {
        customer = customerRepository.save(Customer.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phone("555-1234")
                .address("12 Main St")
                .build());

        product = productRepository.save(Product.builder()
                .sku("TEST-SKU-01")
                .name("Keyboard")
                .description("Mechanical keyboard")
                .price(new BigDecimal("69.99"))
                .stockQuantity(10)
                .category("Electronics")
                .active(true)
                .build());
    }

    @Test
    void createOrder_shouldReduceStockAndCalculateTotal() {
        OrderRequest request = new OrderRequest(
                customer.getId(),
                List.of(new OrderItemRequest(product.getId(), 3))
        );

        OrderResponse response = orderService.createOrder(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("209.97"), response.totalAmount());
        assertEquals("CONFIRMED", response.status());

        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertEquals(7, updatedProduct.getStockQuantity());
    }
}
