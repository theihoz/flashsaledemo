package com.cosmetics.flashsale.control;

import com.cosmetics.flashsale.entity.FlashSaleProductList;

import java.util.List;

/**
 * =======================================================
 * KHỐI ĐIỀU KHIỂN (CONTROL): ProductManagerController
 * Thuộc chức năng: US6 - Thêm mới và Xóa Sản phẩm khỏi Chiến dịch Flash Sale
 * Mục đích: Đóng vai trò là Bộ điều phối quản lý sản phẩm, tiếp nhận
 * yêu cầu từ Boundary và ủy thác xử lý cho Entity FlashSaleProductList.
 * =======================================================
 */
public class ProductManagerController {
    private FlashSaleProductList productList;

    public ProductManagerController() {
        this.productList = new FlashSaleProductList();
    }

    /**
     * Thêm sản phẩm mới vào chiến dịch Flash Sale.
     * Delegate kiểm tra trùng lặp cho Entity.
     */
    public void addProductToCampaign(String productId, double flashSalePrice, int limitQuantity) {
        productList.addProduct(productId, flashSalePrice, limitQuantity);
    }

    /**
     * Xóa sản phẩm khỏi chiến dịch Flash Sale.
     * Delegate cho Entity xử lý.
     */
    public void removeProductFromCampaign(String productId) {
        productList.removeProduct(productId);
    }

    /**
     * Kiểm tra sản phẩm có trong chiến dịch không.
     */
    public boolean isProductInCampaign(String productId) {
        return productList.containsProduct(productId);
    }

    /**
     * Lấy danh sách sản phẩm đang tham gia Flash Sale.
     */
    public List<FlashSaleProductList.FlashSaleProductEntry> getCampaignProducts() {
        return productList.getProducts();
    }

    /**
     * Đếm số sản phẩm trong chiến dịch.
     */
    public int getProductCount() {
        return productList.size();
    }
}
