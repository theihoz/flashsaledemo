package com.cosmetics.flashsale.control;

import com.cosmetics.flashsale.entity.FlashSaleCampaign;
import com.cosmetics.flashsale.database.JsonDatabase;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * =======================================================
 * KHỐI ĐIỀU KHIỂN (CONTROL): CampaignManager
 * Thuộc chức năng: US3 - Thiết lập chiến dịch Flash Sale
 * Mục đích: Đóng vai trò là Bộ quản lý chiến dịch, tiếp nhận
 * yêu cầu từ bảng điều khiển để thiết lập phiên Sale mới.
 * =======================================================
 */
public class CampaignManager {
    private List<FlashSaleCampaign> campaigns = new ArrayList<>();

    public CampaignManager() {
        // Nạp danh sách chiến dịch từ cơ sở dữ liệu JSON
        this.campaigns = new ArrayList<>(JsonDatabase.getInstance().getCampaigns());
    }

    public void createCampaign(LocalDateTime startTime, LocalDateTime endTime, double discountPercent) {
        FlashSaleCampaign campaign = new FlashSaleCampaign(startTime, endTime, discountPercent);
        this.campaigns.add(campaign);
    }

    public List<FlashSaleCampaign> getCampaigns() {
        return campaigns;
    }

    public FlashSaleCampaign getCurrentCampaign() {
        if (campaigns.isEmpty()) return null;
        return campaigns.get(campaigns.size() - 1);
    }
}
