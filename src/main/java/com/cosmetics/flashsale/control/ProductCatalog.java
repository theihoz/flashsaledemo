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
import com.cosmetics.flashsale.database.JsonDatabase;
import com.cosmetics.flashsale.entity.FlashSaleInventory;
import java.util.stream.Collectors;

public class ProductCatalog {
    private List<String> products = new ArrayList<>();

    public ProductCatalog() {
        // Nạp danh sách tên sản phẩm từ cơ sở dữ liệu JSON
        this.products = JsonDatabase.getInstance().getInventories().stream()
                .map(FlashSaleInventory::getProductId)
                .collect(Collectors.toList());
    }
    
    public void addProduct(String product) {
        products.add(product);
    }
    
    public List<String> getProducts() {
        return products;
    }
}
