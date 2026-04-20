package com.cosmetics.flashsale.entity;

import org.junit.Test;
import static org.junit.Assert.*;

public class SaleAnalyticsTest {

    @Test
    public void testGetSoldPercentage_HappyPath() {
        // Xây xới một lô hàng mẫu với tổng 100 mã
        SaleAnalytics analytics = new SaleAnalytics(100);
        // Kê khai số lượng 20 mã hàng đã bán ra với giá lời 50.000 VNĐ/mã
        analytics.recordSale(20, 50000);
        
        // Ép máy tính nhẩm đúng 20% lượng tiêu thụ đạt chuẩn
        assertEquals(20.0, analytics.getSoldPercentage(), 0.01);
        // Ép máy tính xác nhận tổng hoàn tiền doanh số thu 1 triệu VNĐ chính xác
        assertEquals(1000000.0, analytics.getTotalRevenue(), 0.01);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSoldPercentage_UnhappyPath_ZeroInitial() {
        // Có tình tinh chỉnh sức chứa tổng quy về 0 để thử gài bẫy máy tính
        SaleAnalytics analytics = new SaleAnalytics(0);
        
        // Gọi phép tính % để kiểm tra xem hệ thống có bắt dính bẫy lỗi chia cho số 0 mà không sập ứng dụng không
        analytics.getSoldPercentage();
    }
}
