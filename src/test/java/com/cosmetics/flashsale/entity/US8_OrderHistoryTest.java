package com.cosmetics.flashsale.entity;

import org.junit.Test;
import static org.junit.Assert.*;

public class US8_OrderHistoryTest {

    /**
     * [ĐỐI SOÁT NGHIỆP VỤ]
     * User Story: US8 - Lịch sử đơn hàng chi tiết
     * Kịch bản (Scenario): 8.1 - Xem trạng thái đơn hàng Flash Sale chi tiết
     * Luồng xử lý (Path): Happy Path (Luồng thành công)
     * MỤC TIÊU: Xác nhận hệ thống hiển thị đúng thông tin đơn hàng và tính đúng tiền tiết kiệm.
     */
    @Test
    public void testGetOrderHistory_HappyPath() {
        OrderHistory history = new OrderHistory();
        
        // Ghi nhận đơn hàng Flash Sale: Son MAC giá gốc 650.000, mua 520.000, SL 2
        history.recordOrder("Son MAC Ruby Woo", 650000, 520000, 2, true);
        
        // Hệ thống hiển thị đầy đủ thông tin
        assertFalse(history.isEmpty());
        assertEquals(1, history.getOrders().size());
        
        OrderHistory.OrderRecord order = history.getOrders().get(0);
        assertEquals("Son MAC Ruby Woo", order.getProductName());
        assertEquals(520000, order.getPaidPrice(), 0.01);
        assertTrue(order.isFlashSale());
        
        // Tính tiền tiết kiệm: (650.000 - 520.000) * 2 = 260.000 VNĐ
        assertEquals(260000, history.calculateSavedAmount(order), 0.01);
    }

    /**
     * [ĐỐI SOÁT NGHIỆP VỤ]
     * User Story: US8 - Lịch sử đơn hàng chi tiết
     * Kịch bản (Scenario): 8.2 - Khách hàng chưa có lịch sử mua hàng
     * Luồng xử lý (Path): Unhappy Path (Trạng thái rỗng)
     * MỤC TIÊU: Đảm bảo hệ thống trả về danh sách rỗng mà không gây lỗi.
     */
    @Test
    public void testGetOrderHistory_UnhappyPath_Empty() {
        OrderHistory history = new OrderHistory();
        
        // Khách hàng mới đăng ký, chưa mua hàng
        assertTrue(history.isEmpty());
        assertEquals(0, history.getOrders().size());
        assertEquals(0, history.getTotalSavings(), 0.01);
    }

    /**
     * [ĐỐI SOÁT NGHIỆP VỤ]
     * Kịch bản bổ sung: Tính tổng tiết kiệm từ nhiều đơn hàng
     * MỤC TIÊU: Đảm bảo getTotalSavings() cộng dồn chính xác từ tất cả đơn hàng Flash Sale.
     */
    @Test
    public void testGetTotalSavings_MultipleOrders() {
        OrderHistory history = new OrderHistory();
        
        // Đơn 1: Flash Sale - tiết kiệm (650k - 520k) * 2 = 260k
        history.recordOrder("Son MAC Ruby Woo", 650000, 520000, 2, true);
        // Đơn 2: Flash Sale - tiết kiệm (480k - 384k) * 1 = 96k
        history.recordOrder("Kem La Roche", 480000, 384000, 1, true);
        // Đơn 3: Giá thường - tiết kiệm 0
        history.recordOrder("Nước hoa Chanel", 3500000, 3500000, 1, false);
        
        // Tổng tiết kiệm: 260k + 96k = 356k
        assertEquals(356000, history.getTotalSavings(), 0.01);
    }
}
