package com.cosmetics.flashsale.control;

import com.cosmetics.flashsale.entity.FlashSaleCombo;
import com.cosmetics.flashsale.entity.FlashSaleInventory;

import java.util.ArrayList;
import java.util.List;

/**
 * =======================================================
 * KHỐI ĐIỀU KHIỂN (CONTROL): ComboManager
 * Thuộc chức năng: US5 - Quản lý Sản phẩm và Combo Sale
 * Mục đích: Bộ phận điều phối nghiệp vụ tạo mới các Combo,
 * lưu trữ chúng tập trung vào danh mục quản lý của Admin.
 * =======================================================
 */
public class ComboManager {
    private List<FlashSaleCombo> currentCombos = new ArrayList<>();

    public FlashSaleCombo createCombo(String name, List<FlashSaleInventory> products, double originalPrice, double discountPercent) {
        FlashSaleCombo combo = new FlashSaleCombo(name, products, originalPrice, discountPercent);
        currentCombos.add(combo);
        return combo;
    }

    public List<FlashSaleCombo> getCurrentCombos() {
        return currentCombos;
    }
}
