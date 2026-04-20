package com.cosmetics.flashsale.control;

import java.util.ArrayList;
import java.util.List;

/**
 * =======================================================
 * KHỐI ĐIỀU KHIỂN (CONTROL): ProductCatalog
 * Thuộc chức năng: US1 - Hiển thị trạng thái Flash Sale
 * Mục đích: Trình quản lý hiển thị danh mục sản phẩm, giúp 
 * liệt kê các mặt hàng đang áp dụng chương trình Sale.
 * =======================================================
 */
public class ProductCatalog {
    private List<String> products = new ArrayList<>();
    
    public void addProduct(String product) {
        products.add(product);
    }
    
    public List<String> getProducts() {
        return products;
    }
}
