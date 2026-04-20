package com.cosmetics.flashsale.control;

import com.cosmetics.flashsale.entity.SaleAnalytics;
import org.junit.Test;
import static org.junit.Assert.*;

public class DashboardControllerTest {

    @Test
    public void testCalculateSoldPercentage() {
        // Giả lập hệ thống Tracking có sức chứa tổng 50 sản phẩm
        SaleAnalytics analytics = new SaleAnalytics(50);
        // Ghi nhận bán ra 10 sản phẩm, doanh thu mô phỏng là 200.000 VNĐ một sản phẩm
        analytics.recordSale(10, 200000);
        
        DashboardController controller = new DashboardController();
        // Kiểm tra xem Bảng điều khiển (Dashboard) có đọc thuật toán và xuất ra đúng tỷ lệ tiêu thụ 20% không
        assertEquals(20.0, controller.calculateSoldPercentage(analytics), 0.01);
        // Xác minh doanh thu tổng đổ về báo cáo là 2.000.000 VNĐ tròn trĩnh
        assertEquals(2000000.0, controller.getTotalRevenue(analytics), 0.01);
    }
}
