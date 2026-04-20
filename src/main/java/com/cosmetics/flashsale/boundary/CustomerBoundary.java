package com.cosmetics.flashsale.boundary;

import com.cosmetics.flashsale.control.OrderCheckout;
import com.cosmetics.flashsale.entity.FlashSaleCampaign;
import com.cosmetics.flashsale.entity.FlashSaleInventory;

import java.time.LocalDateTime;

/**
 * =======================================================
 * LỚP BIÊN (BOUNDARY): CustomerBoundary
 * Thuộc tầng: Giao diện Khách hàng
 * Nhiệm vụ: Tiếp nhận yêu cầu từ UI/Test và chuyển giao 
 * cho các lớp Control (ProductCatalog, OrderCheckout).
 * =======================================================
 */
public class CustomerBoundary {
    private OrderCheckout orderCheckout = new OrderCheckout();

    /**
     * Lấy giá hiển thị cho sản phẩm dựa trên trạng thái chiến dịch.
     */
    public String getDisplayedPrice(FlashSaleCampaign campaign, String productName) {
        if (campaign != null && campaign.isActive(LocalDateTime.now())) {
            // Giả lập logic hiển thị ứng với US1
            return "1.000.000 VNĐ"; 
        }
        return "1.500.000 VNĐ"; 
    }

    /**
     * Thực hiện quy trình thanh toán.
     */
    public boolean checkout(FlashSaleInventory inventory, int quantity) {
        return orderCheckout.processCheckout(inventory, quantity);
    }
}
