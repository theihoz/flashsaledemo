package com.cosmetics.flashsale.control;

import com.cosmetics.flashsale.entity.FlashSaleCombo;
import com.cosmetics.flashsale.entity.FlashSaleInventory;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class ComboManagerTest {

    @Test
    public void testCreateCombo_Success() {
        ComboManager manager = new ComboManager();
        
        // Khởi tạo các sản phẩm tồn kho như Son MAC (còn 10 cái) và Nước hoa (còn 5 cái)
        FlashSaleInventory p1 = new FlashSaleInventory("Son MAC Ruby Woo", 10);
        FlashSaleInventory p2 = new FlashSaleInventory("Nước hoa Chanel", 5);
        List<FlashSaleInventory> products = Arrays.asList(p1, p2);
        
        // Đẩy lệnh đóng gói Combo
        FlashSaleCombo combo = manager.createCombo("Combo Xịn", products, 1000000.0, 20.0);
        
        // Kiểm tra xem giao dịch tạo Combo thành công chưa (đối tượng không bị trống rỗng)
        assertNotNull(combo);
        // Quản lý kiểm đếm danh sách hiện tại đã lưu trữ đúng 1 mục Combo mới chưa
        assertEquals(1, manager.getCurrentCombos().size());
        // Đảm bảo tên trong danh mục được giữ nguyên vẹn là "Combo Xịn"
        assertEquals("Combo Xịn", manager.getCurrentCombos().get(0).getComboName());
    }
}
