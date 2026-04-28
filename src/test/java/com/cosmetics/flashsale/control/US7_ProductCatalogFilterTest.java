package com.cosmetics.flashsale.control;

import com.cosmetics.flashsale.entity.ProductCategory;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class US7_ProductCatalogFilterTest {

    /**
     * [ĐỐI SOÁT NGHIỆP VỤ]
     * User Story: US7 - Phân loại sản phẩm theo danh mục
     * Kịch bản (Scenario): 7.1 - Lọc danh mục qua Control layer
     * Luồng xử lý (Path): Happy Path (Luồng thành công)
     * MỤC TIÊU: Xác nhận ProductCatalog kết hợp với ProductCategory lọc đúng kết quả.
     */
    @Test
    public void testFilterByCategory_Success() {
        ProductCategory category = new ProductCategory();
        List<ProductCategory.CategorizedProduct> products = Arrays.asList(
            new ProductCategory.CategorizedProduct("Son MAC Ruby Woo", "Son môi", 650000, 10),
            new ProductCategory.CategorizedProduct("Nước hoa Chanel", "Nước hoa", 3500000, 5),
            new ProductCategory.CategorizedProduct("Son Kem Lì Black Rouge", "Son môi", 150000, 20)
        );

        // Control layer gọi Entity để lọc theo danh mục "Son môi"
        List<ProductCategory.CategorizedProduct> result = category.filterByCategory(products, "Son môi");
        assertEquals(2, result.size());
    }

    /**
     * [ĐỐI SOÁT NGHIỆP VỤ]
     * Kịch bản (Scenario): 7.2 - Danh mục rỗng, trả về gợi ý
     * Luồng xử lý (Path): Unhappy Path (Fallback)
     * MỤC TIÊU: Đảm bảo khi danh mục rỗng, Control layer gọi getTopSelling().
     */
    @Test
    public void testFilterByCategory_Fallback_TopSelling() {
        ProductCategory category = new ProductCategory();
        List<ProductCategory.CategorizedProduct> products = Arrays.asList(
            new ProductCategory.CategorizedProduct("Son MAC Ruby Woo", "Son môi", 650000, 30),
            new ProductCategory.CategorizedProduct("Kem chống nắng", "Kem dưỡng", 480000, 15)
        );

        // Bước 1: Lọc theo danh mục "Trang điểm" – không có sản phẩm
        List<ProductCategory.CategorizedProduct> result = category.filterByCategory(products, "Trang điểm");
        assertTrue(result.isEmpty());

        // Bước 2: Fallback - lấy sản phẩm bán chạy nhất
        List<ProductCategory.CategorizedProduct> fallback = category.getTopSelling(products);
        assertFalse(fallback.isEmpty());
        assertEquals("Son MAC Ruby Woo", fallback.get(0).getName());
    }
}
