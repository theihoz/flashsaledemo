package com.cosmetics.flashsale.entity;

import org.junit.Test;
import static org.junit.Assert.*;

public class US6_FlashSaleProductListTest {

    /**
     * [ĐỐI SOÁT NGHIỆP VỤ]
     * User Story: US6 - Thêm mới và Xóa Sản phẩm khỏi Chiến dịch Flash Sale
     * Kịch bản (Scenario): 6.1 - Thêm sản phẩm mỹ phẩm hợp lệ
     * Luồng xử lý (Path): Happy Path (Luồng thành công)
     * MỤC TIÊU: Xác nhận sản phẩm được thêm đúng vào danh sách chiến dịch với giá và số lượng hợp lệ.
     */
    @Test
    public void testAddProduct_HappyPath() {
        FlashSaleProductList list = new FlashSaleProductList();
        
        // Quản lý thêm sản phẩm "Son Kem Lì Black Rouge" với giá Flash Sale 150.000 VNĐ, giới hạn 50 suất
        list.addProduct("Son Kem Lì Black Rouge", 150000, 50);
        
        // Hệ thống xác nhận sản phẩm đã nằm trong danh sách chiến dịch
        assertTrue(list.containsProduct("Son Kem Lì Black Rouge"));
        assertEquals(1, list.size());
        assertEquals(150000, list.getProducts().get(0).getFlashSalePrice(), 0.01);
        assertEquals(50, list.getProducts().get(0).getLimitQuantity());
    }

    /**
     * [ĐỐI SOÁT NGHIỆP VỤ]
     * User Story: US6 - Thêm mới và Xóa Sản phẩm khỏi Chiến dịch Flash Sale
     * Kịch bản (Scenario): 6.2 - Thêm sản phẩm đã tồn tại trong chiến dịch
     * Luồng xử lý (Path): Unhappy Path (Trùng lặp sản phẩm)
     * MỤC TIÊU: Đảm bảo hệ thống chặn việc thêm trùng mã sản phẩm vào cùng một phiên Flash Sale.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddProduct_UnhappyPath_DuplicateProduct() {
        FlashSaleProductList list = new FlashSaleProductList();
        
        // Thêm "Nước tẩy trang Bioderma" lần đầu thành công
        list.addProduct("Nước tẩy trang Bioderma", 200000, 30);
        
        // Quản lý cố gắng thêm lại cùng sản phẩm → Hệ thống phải ném lỗi
        list.addProduct("Nước tẩy trang Bioderma", 180000, 20);
    }

    /**
     * [ĐỐI SOÁT NGHIỆP VỤ]
     * User Story: US6 - Thêm mới và Xóa Sản phẩm khỏi Chiến dịch Flash Sale
     * Kịch bản (Scenario): 6.3 - Xóa sản phẩm khỏi chiến dịch
     * Luồng xử lý (Path): Happy Path (Xóa thành công)
     * MỤC TIÊU: Xác nhận sản phẩm bị loại bỏ khỏi danh sách chiến dịch và giá bán lẻ được khôi phục.
     */
    @Test
    public void testRemoveProduct_HappyPath() {
        FlashSaleProductList list = new FlashSaleProductList();
        
        // Thêm "Mặt nạ ngủ Laneige" vào danh sách chiến dịch
        list.addProduct("Mặt nạ ngủ Laneige", 350000, 20);
        assertTrue(list.containsProduct("Mặt nạ ngủ Laneige"));
        
        // Quản lý nhấn "Xóa" → Hệ thống loại bỏ sản phẩm khỏi danh sách
        list.removeProduct("Mặt nạ ngủ Laneige");
        
        // Sản phẩm không còn trong danh sách chiến dịch
        assertFalse(list.containsProduct("Mặt nạ ngủ Laneige"));
        assertEquals(0, list.size());
    }

    /**
     * [ĐỐI SOÁT NGHIỆP VỤ]
     * Kịch bản bổ sung: Thêm sản phẩm với giá không hợp lệ (giá <= 0)
     * Luồng xử lý (Path): Unhappy Path (Dữ liệu đầu vào sai)
     * MỤC TIÊU: Đảm bảo hệ thống không cho phép tạo sản phẩm với thông tin thiếu hoặc sai.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddProduct_UnhappyPath_InvalidPrice() {
        FlashSaleProductList list = new FlashSaleProductList();
        // Giá Flash Sale bằng 0 → không hợp lệ
        list.addProduct("Son MAC", 0, 10);
    }
}
