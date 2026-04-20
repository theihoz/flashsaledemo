package com.cosmetics.flashsale.control;

import org.junit.Test;
import static org.junit.Assert.*;

public class US1_ProductCatalogTest {

    /**
     * [ĐỐI SOÁT NGHIỆP VỤ]
     * User Story: US1 - Hiển thị trạng thái Flash Sale trên sản phẩm
     * Kịch bản (Scenario): Hỗ trợ quản lý danh mục sản phẩm Flash Sale
     * Luồng xử lý (Path): Happy Path (Thêm sản phẩm thành công)
     * MỤC TIÊU: Đảm bảo lớp Điều khiển (Control) của Danh mục sản phẩm hoạt động chính xác, cho phép nạp và truy xuất dữ liệu sản phẩm để hiển thị trên giao diện.
     */
    @Test
    public void testAddAndGetProduct() {
        ProductCatalog catalog = new ProductCatalog();
        // Điền thử thông tin "Son MAC" vào Danh mục hàng hóa (ngoài 3 sp có sẵn từ JSON)
        catalog.addProduct("Son MAC");
        
        // Kiểm tra chắc chắn danh sách đã giữ được đúng 4 thông tin (3 từ JSON + 1 thêm mới)
        assertEquals(4, catalog.getProducts().size());
        // Quét xem thông tin cuối cùng vừa lưu có khớp chính xác đoạn văn bản là "Son MAC" không
        assertEquals("Son MAC", catalog.getProducts().get(3));
    }
}
