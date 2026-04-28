package com.cosmetics.flashsale.control;

import com.cosmetics.flashsale.entity.OrderHistory;

import java.util.List;

/**
 * =======================================================
 * KHỐI ĐIỀU KHIỂN (CONTROL): OrderHistoryManager
 * Thuộc chức năng: US8 - Lịch sử đơn hàng chi tiết
 * Mục đích: Tiếp nhận yêu cầu truy vấn lịch sử đơn hàng từ Boundary
 * và ủy thác xử lý cho Entity OrderHistory.
 * =======================================================
 */
public class OrderHistoryManager {
    private OrderHistory orderHistory;

    public OrderHistoryManager() {
        this.orderHistory = new OrderHistory();
    }

    /**
     * Ghi nhận đơn hàng mới.
     */
    public void recordOrder(String productName, double originalPrice, double paidPrice, int quantity, boolean isFlashSale) {
        orderHistory.recordOrder(productName, originalPrice, paidPrice, quantity, isFlashSale);
    }

    /**
     * Lấy danh sách đơn hàng.
     */
    public List<OrderHistory.OrderRecord> getOrderDetails() {
        return orderHistory.getOrders();
    }

    /**
     * Kiểm tra trạng thái rỗng.
     */
    public boolean hasNoOrders() {
        return orderHistory.isEmpty();
    }

    /**
     * Tính tổng tiết kiệm.
     */
    public double getSavingsSummary() {
        return orderHistory.getTotalSavings();
    }
}
