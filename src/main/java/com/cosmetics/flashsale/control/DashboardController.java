package com.cosmetics.flashsale.control;

import com.cosmetics.flashsale.entity.SaleAnalytics;

/**
 * =======================================================
 * KHỐI ĐIỀU KHIỂN (CONTROL): DashboardController
 * Thuộc chức năng: US4 - Báo cáo hiệu quả thời gian thực
 * Mục đích: Tiếp nhận số liệu phân tích và kết xuất lên 
 * Tấm nền (Dashboard) cho nhân sự Quản lý cửa hàng theo dõi.
 * =======================================================
 */
public class DashboardController {
    
    public double calculateSoldPercentage(SaleAnalytics analytics) {
        return analytics.getSoldPercentage();
    }
    
    public double getTotalRevenue(SaleAnalytics analytics) {
        return analytics.getTotalRevenue();
    }
}
