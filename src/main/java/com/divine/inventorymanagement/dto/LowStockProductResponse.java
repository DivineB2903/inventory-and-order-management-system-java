package com.divine.inventorymanagement.dto;

import java.math.BigDecimal;
import java.util.List;

public record LowStockProductResponse(
        Long id,
        String sku,
        String name,
        Integer stockQuantity,
        String category
) {
}
