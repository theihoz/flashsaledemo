package com.cosmetics.flashsale.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * =======================================================
 * MÔ HÌNH THỰC THỂ (ENTITY): OrderHistory
 * Thuộc chức năng: US8 - Lịch sử đơn hàng chi tiết
 * 
 * QUY TẮC NGHIỆP VỤ (BUSINESS RULES):
 * 1. Thông tin "Số tiền tiết kiệm" phải tính dựa trên chênh lệch giá gốc và giá Flash Sale.
 * 2. Chỉ hiển thị lịch sử cho người dùng đã đăng nhập hoặc có mã đơn hàng hợp lệ.
 * 
 * LOGIC XỬ LÝ (BUSINESS LOGIC):
 * - Lưu trữ danh sách đơn hàng và cung cấp phương thức tính tiết kiệm.
 * - Trả về danh sách rỗng nếu chưa có giao dịch (thay vì lỗi hệ thống).
 * =======================================================
 */
public class OrderHistory {

    /**
     * Cấu trúc đại diện cho một đơn hàng đã hoàn tất.
     */
    public static class OrderRecord {
        private String productName;
        private double originalPrice;
        private double paidPrice;
        private int quantity;
        private boolean isFlashSale;

        public OrderRecord(String productName, double originalPrice, double paidPrice, int quantity, boolean isFlashSale) {
            this.productName = productName;
            this.originalPrice = originalPrice;
            this.paidPrice = paidPrice;
            this.quantity = quantity;
            this.isFlashSale = isFlashSale;
        }

        public String getProductName() { return productName; }
        public double getOriginalPrice() { return originalPrice; }
        public double getPaidPrice() { return paidPrice; }
        public int getQuantity() { return quantity; }
        public boolean isFlashSale() { return isFlashSale; }
    }

    private List<OrderRecord> orders = new ArrayList<>();

    /**
     * Ghi nhận đơn hàng mới vào lịch sử.
     */
    public void recordOrder(String productName, double originalPrice, double paidPrice, int quantity, boolean isFlashSale) {
        orders.add(new OrderRecord(productName, originalPrice, paidPrice, quantity, isFlashSale));
    }

    /**
     * Lấy toàn bộ danh sách đơn hàng.
     */
    public List<OrderRecord> getOrders() {
        return new ArrayList<>(orders);
    }

    /**
     * Kiểm tra xem có đơn hàng nào không.
     */
    public boolean isEmpty() {
        return orders.isEmpty();
    }

    /**
     * Tính số tiền tiết kiệm cho một đơn hàng.
     */
    public double calculateSavedAmount(OrderRecord order) {
        if (!order.isFlashSale()) return 0;
        return (order.getOriginalPrice() - order.getPaidPrice()) * order.getQuantity();
    }

    /**
     * Tính tổng số tiền tiết kiệm từ tất cả đơn hàng Flash Sale.
     */
    public double getTotalSavings() {
        return orders.stream()
                .filter(OrderRecord::isFlashSale)
                .mapToDouble(this::calculateSavedAmount)
                .sum();
    }
}
