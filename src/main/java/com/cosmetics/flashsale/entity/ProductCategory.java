package com.cosmetics.flashsale.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * =======================================================
 * MÔ HÌNH THỰC THỂ (ENTITY): ProductCategory
 * Thuộc chức năng: US7 - Phân loại sản phẩm theo danh mục
 * 
 * QUY TẮC NGHIỆP VỤ (BUSINESS RULES):
 * 1. Hệ thống chỉ hiển thị các danh mục có sản phẩm đang áp dụng Flash Sale.
 * 2. Nếu danh mục không có sản phẩm, cung cấp gợi ý thay thế (Fallback).
 * 
 * LOGIC XỬ LÝ (BUSINESS LOGIC):
 * - Lọc dữ liệu theo thẻ danh mục (category tag) của từng sản phẩm.
 * - Tự động quét và đề xuất sản phẩm "Hot Sale" nếu danh mục rỗng.
 * =======================================================
 */
public class ProductCategory {

    /**
     * Cấu trúc đại diện cho một sản phẩm có danh mục.
     */
    public static class CategorizedProduct {
        private String name;
        private String category;
        private double price;
        private int soldCount;

        public CategorizedProduct(String name, String category, double price, int soldCount) {
            this.name = name;
            this.category = category;
            this.price = price;
            this.soldCount = soldCount;
        }

        public String getName() { return name; }
        public String getCategory() { return category; }
        public double getPrice() { return price; }
        public int getSoldCount() { return soldCount; }
    }

    /**
     * Lọc sản phẩm theo danh mục.
     * @return danh sách sản phẩm thuộc danh mục chỉ định, hoặc rỗng nếu không tìm thấy.
     */
    public List<CategorizedProduct> filterByCategory(List<CategorizedProduct> products, String category) {
        if (category == null || category.trim().isEmpty()) {
            return products;
        }
        return products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách các danh mục có sản phẩm đang bán.
     */
    public List<String> getAvailableCategories(List<CategorizedProduct> products) {
        return products.stream()
                .map(CategorizedProduct::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách sản phẩm bán chạy nhất (Fallback recommendation).
     * Sắp xếp theo số lượng đã bán giảm dần, trả về top 3.
     */
    public List<CategorizedProduct> getTopSelling(List<CategorizedProduct> products) {
        return products.stream()
                .sorted((a, b) -> Integer.compare(b.getSoldCount(), a.getSoldCount()))
                .limit(3)
                .collect(Collectors.toList());
    }
}
