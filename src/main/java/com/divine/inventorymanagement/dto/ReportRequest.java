package com.divine.inventorymanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ReportRequest(
        @NotNull(message = "A threshold is required") Integer lowStockThreshold,
        @NotBlank(message = "Report type is required") String reportType
) {
}
