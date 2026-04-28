package com.cosmetics.flashsale.control;

import org.junit.Test;
import static org.junit.Assert.*;

public class US6_ProductManagerControllerTest {

    /**
     * [ĐỐI SOÁT NGHIỆP VỤ]
     * User Story: US6 - Thêm mới và Xóa Sản phẩm khỏi Chiến dịch Flash Sale
     * Kịch bản (Scenario): 6.1 - Thêm sản phẩm hợp lệ qua Controller
     * Luồng xử lý (Path): Happy Path (Luồng thành công)
     * MỤC TIÊU: Xác nhận Controller điều phối việc thêm sản phẩm và cập nhật danh sách đúng.
     */
    @Test
    public void testAddProduct_Success() {
        ProductManagerController controller = new ProductManagerController();
        
        // Quản lý thêm sản phẩm qua bộ điều phối
        controller.addProductToCampaign("Son Kem Lì Black Rouge", 150000, 50);
        
        // Kiểm tra sản phẩm đã được đăng ký trong chiến dịch
        assertTrue(controller.isProductInCampaign("Son Kem Lì Black Rouge"));
        assertEquals(1, controller.getProductCount());
    }

    /**
     * [ĐỐI SOÁT NGHIỆP VỤ]
     * User Story: US6 - Thêm mới và Xóa Sản phẩm khỏi Chiến dịch Flash Sale
     * Kịch bản (Scenario): 6.2 - Thêm sản phẩm đã tồn tại qua Controller
     * Luồng xử lý (Path): Unhappy Path (Trùng lặp)
     * MỤC TIÊU: Đảm bảo Controller truyền lỗi từ Entity lên đúng cách.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddProduct_Failure_Duplicate() {
        ProductManagerController controller = new ProductManagerController();
        
        controller.addProductToCampaign("Nước tẩy trang Bioderma", 200000, 30);
        // Thêm trùng → Controller phải để Entity báo lỗi
        controller.addProductToCampaign("Nước tẩy trang Bioderma", 180000, 20);
    }

    /**
     * [ĐỐI SOÁT NGHIỆP VỤ]
     * User Story: US6 - Thêm mới và Xóa Sản phẩm khỏi Chiến dịch Flash Sale
     * Kịch bản (Scenario): 6.3 - Xóa sản phẩm qua Controller
     * Luồng xử lý (Path): Happy Path (Xóa thành công)
     * MỤC TIÊU: Xác nhận Controller xóa được sản phẩm và danh sách giảm.
     */
    @Test
    public void testRemoveProduct_Success() {
        ProductManagerController controller = new ProductManagerController();
        
        controller.addProductToCampaign("Mặt nạ ngủ Laneige", 350000, 20);
        assertEquals(1, controller.getProductCount());
        
        // Xóa sản phẩm qua Controller
        controller.removeProductFromCampaign("Mặt nạ ngủ Laneige");
        
        assertFalse(controller.isProductInCampaign("Mặt nạ ngủ Laneige"));
        assertEquals(0, controller.getProductCount());
    }
}
