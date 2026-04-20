package com.cosmetics.flashsale.entity;

import java.util.List;

/**
 * =======================================================
 * MÔ HÌNH THỰC THỂ (ENTITY): FlashSaleCombo
 * Thuộc chức năng: US5 - Quản lý Sản phẩm và Combo Sale
 * Mục đích: Cấu trúc dữ liệu Gói Combo chứa nhiều mặt hàng,
 * tự động tính lại giá gốc thành giá chiết khấu chung.
 * =======================================================
 */
public class FlashSaleCombo {
    private String comboName;
    private List<FlashSaleInventory> products;
    private double originalPrice;
    private double discountPercent;

    public FlashSaleCombo(String comboName, List<FlashSaleInventory> products, double originalPrice, double discountPercent) {
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Combo phải có ít nhất 1 sản phẩm");
        }
        for (FlashSaleInventory product : products) {
            if (product.getAvailableQuantity() <= 0) {
                throw new IllegalStateException("Sản phẩm " + product.getProductId() + " không đủ tồn kho để tạo Combo");
            }
        }
        this.comboName = comboName;
        this.products = products;
        this.originalPrice = originalPrice;
        this.discountPercent = discountPercent;
    }

    public double getDiscountedPrice() {
        return originalPrice * (1 - discountPercent / 100);
    }

    public int getAvailableComboQuantity() {
        return products.stream()
            .mapToInt(FlashSaleInventory::getAvailableQuantity)
            .min()
            .orElse(0);
    }
    
    public String getComboName() {
        return comboName;
    }
    
    public List<FlashSaleInventory> getProducts() {
        return products;
    }
}
