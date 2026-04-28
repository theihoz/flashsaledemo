package com.cosmetics.flashsale.control;

import com.cosmetics.flashsale.entity.OrderHistory;
import org.junit.Test;
import static org.junit.Assert.*;

public class US8_OrderHistoryManagerTest {

    /**
     * [ĐỐI SOÁT NGHIỆP VỤ]
     * User Story: US8 - Lịch sử đơn hàng chi tiết
     * Kịch bản (Scenario): 8.1 - Xem lịch sử đơn hàng qua Controller
     * Luồng xử lý (Path): Happy Path (Luồng thành công)
     * MỤC TIÊU: Xác nhận Controller truy vấn đúng dữ liệu từ Entity.
     */
    @Test
    public void testGetOrderDetails_Success() {
        OrderHistoryManager manager = new OrderHistoryManager();
        
        manager.recordOrder("Son MAC Ruby Woo", 650000, 520000, 2, true);
        
        assertFalse(manager.hasNoOrders());
        assertEquals(1, manager.getOrderDetails().size());
        assertEquals("Son MAC Ruby Woo", manager.getOrderDetails().get(0).getProductName());
    }

    /**
     * [ĐỐI SOÁT NGHIỆP VỤ]
     * Kịch bản (Scenario): 8.2 - Khách hàng chưa mua hàng
     * Luồng xử lý (Path): Unhappy Path (Trạng thái rỗng)
     * MỤC TIÊU: Đảm bảo Controller trả về trạng thái rỗng đúng cách.
     */
    @Test
    public void testGetOrderDetails_Empty() {
        OrderHistoryManager manager = new OrderHistoryManager();
        
        assertTrue(manager.hasNoOrders());
        assertEquals(0, manager.getOrderDetails().size());
        assertEquals(0, manager.getSavingsSummary(), 0.01);
    }

    /**
     * [ĐỐI SOÁT NGHIỆP VỤ]
     * Kịch bản bổ sung: Tính tổng tiết kiệm qua Controller
     * MỤC TIÊU: Xác nhận Controller tính tổng tiết kiệm chính xác.
     */
    @Test
    public void testGetSavingsSummary() {
        OrderHistoryManager manager = new OrderHistoryManager();
        
        manager.recordOrder("Son MAC", 650000, 520000, 1, true);  // Tiết kiệm 130k
        manager.recordOrder("Kem dưỡng", 480000, 384000, 1, true); // Tiết kiệm 96k
        
        assertEquals(226000, manager.getSavingsSummary(), 0.01);
    }
}
