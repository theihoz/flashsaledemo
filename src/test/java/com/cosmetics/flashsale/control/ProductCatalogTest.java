package com.cosmetics.flashsale.control;

import org.junit.Test;
import static org.junit.Assert.*;

public class ProductCatalogTest {

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
