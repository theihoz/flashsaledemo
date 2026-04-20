package com.cosmetics.flashsale.entity;

/**
 * =======================================================
 * MÔ HÌNH THỰC THỂ (ENTITY): SaleAnalytics
 * Thuộc chức năng: US4 - Báo cáo hiệu quả thời gian thực
 * 
 * QUY TẮC NGHIỆP VỤ (BUSINESS RULES):
 * 1. Dữ liệu báo cáo phải được cập nhật ngay sau khi thanh toán thành công (Real-time).
 * 2. Ngăn chặn lỗi chia cho 0 nếu chưa cấu hình số lượng khởi tạo.
 * 
 * LOGIC XỬ LÝ (BUSINESS LOGIC):
 * - Cơ chế 'Cập nhật cộng dồn' giúp ghi nhận doanh thu tức thì.
 * - Kiểm tra mẫu số (totalInitialInventory) trước khi tính tỷ lệ phần trăm (Protect logic).
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
