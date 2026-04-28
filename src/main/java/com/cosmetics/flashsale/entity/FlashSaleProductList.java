package com.cosmetics.flashsale.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * =======================================================
 * MÔ HÌNH THỰC THỂ (ENTITY): FlashSaleProductList
 * Thuộc chức năng: US6 - Thêm mới và Xóa Sản phẩm khỏi Chiến dịch Flash Sale
 * 
 * QUY TẮC NGHIỆP VỤ (BUSINESS RULES):
 * 1. Một mã sản phẩm không thể được thêm hai lần vào cùng một phiên Flash Sale.
 * 2. Sản phẩm khi thêm phải có giá Flash Sale và giới hạn số lượng bán ra.
 * 3. Khi xóa sản phẩm, nó phải được loại bỏ khỏi danh sách chiến dịch.
 * 
 * LOGIC XỬ LÝ (BUSINESS LOGIC):
 * - Kiểm tra trùng lặp mã sản phẩm trước khi thêm (Check-then-Add).
 * - Sử dụng LinkedHashMap để duy trì thứ tự thêm vào, giúp hiển thị nhất quán.
 * =======================================================
 */
public class FlashSaleProductList {

    /**
     * Cấu trúc lưu trữ một sản phẩm trong danh sách Flash Sale.
     */
    public static class FlashSaleProductEntry {
        private String productId;
        private double flashSalePrice;
        private int limitQuantity;

        public FlashSaleProductEntry(String productId, double flashSalePrice, int limitQuantity) {
            this.productId = productId;
            this.flashSalePrice = flashSalePrice;
            this.limitQuantity = limitQuantity;
        }

        public String getProductId() { return productId; }
        public double getFlashSalePrice() { return flashSalePrice; }
        public int getLimitQuantity() { return limitQuantity; }
    }

    private Map<String, FlashSaleProductEntry> productMap = new LinkedHashMap<>();

    /**
     * Thêm sản phẩm vào danh sách Flash Sale.
     * @throws IllegalArgumentException nếu sản phẩm đã tồn tại trong chiến dịch.
     */
    public void addProduct(String productId, double flashSalePrice, int limitQuantity) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã sản phẩm không được để trống");
        }
        if (flashSalePrice <= 0) {
            throw new IllegalArgumentException("Giá Flash Sale phải lớn hơn 0");
        }
        if (limitQuantity <= 0) {
            throw new IllegalArgumentException("Số lượng giới hạn phải lớn hơn 0");
        }
        if (productMap.containsKey(productId)) {
            throw new IllegalArgumentException("Sản phẩm đã tồn tại trong chiến dịch Flash Sale này");
        }
        productMap.put(productId, new FlashSaleProductEntry(productId, flashSalePrice, limitQuantity));
    }

    /**
     * Xóa sản phẩm khỏi danh sách Flash Sale.
     * @throws IllegalArgumentException nếu sản phẩm không tồn tại trong chiến dịch.
     */
    public void removeProduct(String productId) {
        if (!productMap.containsKey(productId)) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại trong chiến dịch Flash Sale");
        }
        productMap.remove(productId);
    }

    /**
     * Kiểm tra xem sản phẩm đã có trong danh sách chưa.
     */
    public boolean containsProduct(String productId) {
        return productMap.containsKey(productId);
    }

    /**
     * Lấy toàn bộ danh sách sản phẩm đang tham gia Flash Sale.
     */
    public List<FlashSaleProductEntry> getProducts() {
        return new ArrayList<>(productMap.values());
    }

    /**
     * Đếm số lượng sản phẩm trong danh sách.
     */
    public int size() {
        return productMap.size();
    }
}
