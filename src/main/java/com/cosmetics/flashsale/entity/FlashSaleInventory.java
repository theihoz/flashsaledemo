package com.cosmetics.flashsale.entity;

/**
 * =======================================================
 * MÔ HÌNH THỰC THỂ (ENTITY): FlashSaleInventory
 * Thuộc chức năng: US2 - Xử lý tồn kho và thanh toán
 * Mục đích: Lưu trữ số lượng sản phẩm đang có trong kho ảo
 * và xử lý nghiệp vụ trừ kho an toàn (Synchronized) chống kẹt.
 * =======================================================
 */
public class FlashSaleInventory {
    private String productId;
    private int availableQuantity;

    public FlashSaleInventory(String productId, int availableQuantity) {
        this.productId = productId;
        this.availableQuantity = availableQuantity;
    }

    public synchronized boolean holdInventory(int quantity) {
        if (availableQuantity < quantity) {
            throw new IllegalStateException("Sản phẩm đã hết suất Flash Sale");
        }
        availableQuantity -= quantity;
        return true;
    }
    
    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public String getProductId() {
        return productId;
    }
}
