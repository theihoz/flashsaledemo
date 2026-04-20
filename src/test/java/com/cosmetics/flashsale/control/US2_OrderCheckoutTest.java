package com.cosmetics.flashsale.control;

import com.cosmetics.flashsale.entity.FlashSaleInventory;
import org.junit.Test;
import static org.junit.Assert.*;

public class US2_OrderCheckoutTest {

    /**
     * [ĐỐI SOÁT NGHIỆP VỤ]
     * User Story: US2 - Xử lý tồn kho và thanh toán
     * Kịch bản (Scenario): 2.1 - Thanh toán hợp lệ
     * Luồng xử lý (Path): Happy Path (Thanh toán thành công)
     * MỤC TIÊU: Xác nhận logic của lớp Điều khiển (Control) trong việc phối hợp với Entity để trừ kho và hoàn tất giao dịch khi đủ điều kiện.
     */
    @Test
    public void testCheckout_Success() {
        // Tạo kho giả lập cho Son Môi chứa sẵn 5 đơn vị
        FlashSaleInventory inventory = new FlashSaleInventory("LIPSTICK_01", 5);
        OrderCheckout checkout = new OrderCheckout();
        
        // Gửi lệnh thanh toán chốt mua 2 sản phẩm và xác nhận hệ thống báo giao dịch trót lọt
        assertTrue(checkout.processCheckout(inventory, 2));
        // Xác minh xem số dư kho hàng đã được trừ đi sát sao và còn đúng 3 đơn vị không
        assertEquals(3, inventory.getAvailableQuantity());
    }

    /**
     * [ĐỐI SOÁT NGHIỆP VỤ]
     * User Story: US2 - Xử lý tồn kho và thanh toán
     * Kịch bản (Scenario): 2.2 - Thanh toán khi hết kho khuyến mãi
     * Luồng xử lý (Path): Unhappy Path (Sản phẩm hết suất Flash Sale)
     * MỤC TIÊU: Đảm bảo lớp Điều khiển chặn đứng giao dịch và thông báo chính xác cho người dùng khi kho hàng thực tế đã về 0.
     */
    @Test(expected = IllegalStateException.class)
    public void testCheckout_Fail_OutOfStock() {
        // Ép số lượng kho của túi hàng này chỉ có vỏn vẹn 1
        FlashSaleInventory inventory = new FlashSaleInventory("LIPSTICK_01", 1);
        OrderCheckout checkout = new OrderCheckout();
        
        // Cố tình đẩy lệnh đặt 2 sản phẩm để thử thách hệ thống tung biển cấm "Từ chối" vì quá sức chứa
        checkout.processCheckout(inventory, 2);
    }
}
