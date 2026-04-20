package com.cosmetics.flashsale.entity;

import org.junit.Test;
import static org.junit.Assert.*;

public class FlashSaleInventoryTest {

    @Test
    public void testHoldInventory_HappyPath() {
        // Hệ thống bày ra sức chứa 10 chỗ trống ảo (Kho hàng P1 có 10 sản phẩm)
        FlashSaleInventory inventory = new FlashSaleInventory("P1", 10);
        
        // Cho một đối tượng khách ảo xí trước 2 chỗ, máy sẽ kiểm định xem hệ thống có ghi nhận báo thành công
        assertTrue(inventory.holdInventory(2));
        // Kiểm tra thực xuất kho xem số lượng có giảm chuẩn xuống 8 không
        assertEquals(8, inventory.getAvailableQuantity());
    }

    @Test(expected = IllegalStateException.class)
    public void testHoldInventory_UnhappyPath_OutOfStock() {
        // Mô phỏng kho hàng P1 lèo tèo chỉ còn 1 sản phẩm
        FlashSaleInventory inventory = new FlashSaleInventory("P1", 1);
        
        // Khách cố tình mua 2 món cùng lúc. Mã kiểm thử theo dõi sát sao để ép hệ thống tung ra cảnh báo Lỗi không đủ hàng
        inventory.holdInventory(2);
    }
}
