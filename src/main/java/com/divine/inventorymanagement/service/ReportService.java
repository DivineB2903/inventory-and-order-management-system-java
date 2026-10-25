package com.divine.inventorymanagement.service;

import com.divine.inventorymanagement.dto.LowStockProductResponse;
import com.divine.inventorymanagement.dto.SalesSummaryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    private final ProductService productService;
    private final OrderService orderService;

    public ReportService(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    public List<LowStockProductResponse> getLowStockProducts(Integer threshold) {
        return productService.getLowStockProducts(threshold);
    }

    public SalesSummaryResponse getSalesSummary(Integer lowStockThreshold) {
        return orderService.getSalesSummary(lowStockThreshold);
    }
}
