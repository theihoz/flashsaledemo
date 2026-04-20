package com.cosmetics.flashsale.control;

import com.cosmetics.flashsale.entity.FlashSaleInventory;

/**
 * =======================================================
 * KHỐI ĐIỀU KHIỂN (CONTROL): OrderCheckout
 * Thuộc chức năng: US2 - Xử lý tồn kho và thanh toán
 * Mục đích: Bộ xử lý luồng thanh toán, tiếp nhận số lượng
 * khách hàng muốn mua và giao tiếp với Thực thể Kho (Inventory).
 * =======================================================
 */
public class OrderCheckout {
    public boolean processCheckout(FlashSaleInventory inventory, int requestedQuantity) {
        if (inventory == null) {
            throw new IllegalArgumentException("Không tìm thấy thông tin kho");
        }
        return inventory.holdInventory(requestedQuantity);
    }
}
