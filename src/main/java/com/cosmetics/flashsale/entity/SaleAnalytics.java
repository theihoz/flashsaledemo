package com.cosmetics.flashsale.entity;

/**
 * =======================================================
 * MÔ HÌNH THỰC THỂ (ENTITY): SaleAnalytics
 * Thuộc chức năng: US4 - Báo cáo hiệu quả thời gian thực
 * Mục đích: Nắm giữ dữ liệu phân tích, đo lường tự động
 * phần trăm sản phẩm bán ra và tính tổng doạnh thu vận hành.
 * =======================================================
 */
public class SaleAnalytics {
    private int totalInitialInventory;
    private int totalSold;
    private double totalRevenue;

    public SaleAnalytics(int totalInitialInventory) {
        this.totalInitialInventory = totalInitialInventory;
        this.totalSold = 0;
        this.totalRevenue = 0;
    }

    public synchronized void recordSale(int quantity, double price) {
        this.totalSold += quantity;
        this.totalRevenue += (quantity * price);
    }

    public double getSoldPercentage() {
        if (totalInitialInventory <= 0) {
            throw new IllegalStateException("Chưa cấu hình số lượng tổng");
        }
        return ((double) totalSold / totalInitialInventory) * 100;
    }
    
    public double getTotalRevenue() {
        return totalRevenue;
    }
}
