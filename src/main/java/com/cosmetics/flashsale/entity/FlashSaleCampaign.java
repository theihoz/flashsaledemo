package com.cosmetics.flashsale.entity;

import java.time.LocalDateTime;

/**
 * =======================================================
 * MÔ HÌNH THỰC THỂ (ENTITY): FlashSaleCampaign
 * Thuộc chức năng: US3 - Thiết lập chiến dịch Flash Sale
 * Mục đích: Lưu trữ cấu hình chiến dịch, đảm bảo quy định 
 * giảm giá không vượt 50% và thời gian diễn ra hợp lệ.
 * =======================================================
 */
public class FlashSaleCampaign {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double discountPercent;
    
    public FlashSaleCampaign(LocalDateTime startTime, LocalDateTime endTime, double discountPercent) {
        if (discountPercent > 50.0) {
            throw new IllegalArgumentException("Mức giảm không được vượt quá 50%");
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Thời gian bắt đầu phải trước thời gian kết thúc");
        }
        this.startTime = startTime;
        this.endTime = endTime;
        this.discountPercent = discountPercent;
    }
    
    public boolean isActive(LocalDateTime currentTime) {
        return currentTime.compareTo(startTime) >= 0 && currentTime.compareTo(endTime) <= 0;
    }
    
    public double getDiscountPercent() {
        return discountPercent;
    }
}
