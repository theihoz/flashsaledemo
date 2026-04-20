package com.cosmetics.flashsale.control;

import com.cosmetics.flashsale.entity.FlashSaleCampaign;
import java.time.LocalDateTime;

/**
 * =======================================================
 * KHỐI ĐIỀU KHIỂN (CONTROL): CampaignManager
 * Thuộc chức năng: US3 - Thiết lập chiến dịch Flash Sale
 * Mục đích: Đóng vai trò là Bộ quản lý chiến dịch, tiếp nhận
 * yêu cầu từ bảng điều khiển để thiết lập phiên Sale mới.
 * =======================================================
 */
public class CampaignManager {
    private FlashSaleCampaign currentCampaign;

    public void createCampaign(LocalDateTime startTime, LocalDateTime endTime, double discountPercent) {
        FlashSaleCampaign campaign = new FlashSaleCampaign(startTime, endTime, discountPercent);
        this.currentCampaign = campaign;
    }

    public FlashSaleCampaign getCurrentCampaign() {
        return currentCampaign;
    }
}
