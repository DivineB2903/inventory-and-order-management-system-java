package com.divine.inventorymanagement.dto;

import java.math.BigDecimal;
import java.util.List;

public record SalesSummaryResponse(
        long orderCount,
        BigDecimal totalRevenue,
        BigDecimal averageOrderValue,
        List<LowStockProductResponse> lowStockProducts
) {
}
