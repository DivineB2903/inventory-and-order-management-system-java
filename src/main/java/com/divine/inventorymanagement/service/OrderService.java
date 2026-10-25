package com.divine.inventorymanagement.service;

import com.divine.inventorymanagement.dto.*;
import com.divine.inventorymanagement.exception.InsufficientStockException;
import com.divine.inventorymanagement.exception.ResourceNotFoundException;
import com.divine.inventorymanagement.model.*;
import com.divine.inventorymanagement.repository.CustomerRepository;
import com.divine.inventorymanagement.repository.OrderRepository;
import com.divine.inventorymanagement.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                       CustomerRepository customerRepository,
                       ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.customerId()));

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.ZERO);

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemRequest.productId()));

            if (!product.isActive()) {
                throw new ResourceNotFoundException("Product is inactive and cannot be ordered: " + product.getName());
            }

            if (product.getStockQuantity() < itemRequest.quantity()) {
                throw new InsufficientStockException("Insufficient stock for product " + product.getName()
                        + ". Available: " + product.getStockQuantity() + ", requested: " + itemRequest.quantity());
            }

            BigDecimal unitPrice = product.getPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(itemRequest.quantity()));
            total = total.add(subtotal);

            OrderItem item = OrderItem.builder()
                    .product(product)
                    .quantity(itemRequest.quantity())
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .build();

            order.addItem(item);
            product.setStockQuantity(product.getStockQuantity() - itemRequest.quantity());
        }

        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(total);
        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return mapToResponse(order);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if (newStatus == OrderStatus.CANCELLED && order.getStatus() != OrderStatus.CANCELLED) {
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            }
        }

        order.setStatus(newStatus);
        return mapToResponse(orderRepository.save(order));
    }

    public SalesSummaryResponse getSalesSummary(Integer lowStockThreshold) {
        List<Order> orders = orderRepository.findAll();
        BigDecimal totalRevenue = orders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageOrderValue = orders.isEmpty() ? BigDecimal.ZERO : totalRevenue.divide(BigDecimal.valueOf(orders.size()), 2, java.math.RoundingMode.HALF_UP);

        List<LowStockProductResponse> lowStockProducts = productRepository.findByStockQuantityLessThan(lowStockThreshold == null ? 5 : lowStockThreshold)
                .stream()
                .map(product -> new LowStockProductResponse(
                        product.getId(),
                        product.getSku(),
                        product.getName(),
                        product.getStockQuantity(),
                        product.getCategory()))
                .toList();

        return new SalesSummaryResponse(orders.size(), totalRevenue, averageOrderValue, lowStockProducts);
    }

    private OrderResponse mapToResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getSubtotal()))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getCustomer().getId(),
                order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                itemResponses
        );
    }
}
