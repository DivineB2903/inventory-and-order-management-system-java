package com.divine.inventorymanagement.controller;

import com.divine.inventorymanagement.dto.LowStockProductResponse;
import com.divine.inventorymanagement.dto.SalesSummaryResponse;
import com.divine.inventorymanagement.service.ReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/reports/low-stock")
    public List<LowStockProductResponse> getLowStockProducts(@RequestParam(defaultValue = "5") Integer threshold) {
        return reportService.getLowStockProducts(threshold);
    }

    @GetMapping("/reports/sales-summary")
    public SalesSummaryResponse getSalesSummary(@RequestParam(defaultValue = "5") Integer lowStockThreshold) {
        return reportService.getSalesSummary(lowStockThreshold);
    }
}
