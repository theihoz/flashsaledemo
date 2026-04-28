package com.cosmetics.flashsale.entity;

import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class US7_ProductCategoryTest {

    /**
     * [ĐỐI SOÁT NGHIỆP VỤ]
     * User Story: US7 - Phân loại sản phẩm theo danh mục
     * Kịch bản (Scenario): 7.1 - Lọc danh mục mỹ phẩm thành công
     * Luồng xử lý (Path): Happy Path (Luồng thành công)
     * MỤC TIÊU: Xác nhận hệ thống chỉ trả về sản phẩm thuộc danh mục được chọn.
     */
    @Test
    public void testFilterByCategory_HappyPath() {
        ProductCategory category = new ProductCategory();
        List<ProductCategory.CategorizedProduct> products = Arrays.asList(
            new ProductCategory.CategorizedProduct("Son MAC Ruby Woo", "Son môi", 650000, 10),
            new ProductCategory.CategorizedProduct("Nước hoa Chanel N°5", "Nước hoa", 3500000, 5),
            new ProductCategory.CategorizedProduct("Son Kem Lì Black Rouge", "Son môi", 150000, 20)
        );

        // Khách hàng chọn bộ lọc "Son môi"
        List<ProductCategory.CategorizedProduct> result = category.filterByCategory(products, "Son môi");

        // Chỉ hiển thị 2 sản phẩm thuộc danh mục "Son môi"
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.getCategory().equals("Son môi")));
    }

    /**
     * [ĐỐI SOÁT NGHIỆP VỤ]
     * User Story: US7 - Phân loại sản phẩm theo danh mục
     * Kịch bản (Scenario): 7.2 - Danh mục không có sản phẩm Flash Sale
     * Luồng xử lý (Path): Unhappy Path (Danh mục rỗng → Fallback)
     * MỤC TIÊU: Khi danh mục rỗng, hệ thống phải trả về danh sách Bán chạy nhất.
     */
    @Test
    public void testFilterByCategory_UnhappyPath_EmptyCategory() {
        ProductCategory category = new ProductCategory();
        List<ProductCategory.CategorizedProduct> products = Arrays.asList(
            new ProductCategory.CategorizedProduct("Son MAC Ruby Woo", "Son môi", 650000, 30),
            new ProductCategory.CategorizedProduct("Kem La Roche", "Kem dưỡng", 480000, 15)
        );

        // Khách chọn "Nước hoa" nhưng đợt sale không có sản phẩm nào thuộc danh mục này
        List<ProductCategory.CategorizedProduct> result = category.filterByCategory(products, "Nước hoa");
        assertTrue(result.isEmpty());

        // Hệ thống tự động đề xuất danh sách "Bán chạy nhất"
        List<ProductCategory.CategorizedProduct> topSelling = category.getTopSelling(products);
        assertFalse(topSelling.isEmpty());
        // Sản phẩm bán chạy nhất phải đứng đầu
        assertEquals("Son MAC Ruby Woo", topSelling.get(0).getName());
    }

    /**
     * [ĐỐI SOÁT NGHIỆP VỤ]
     * Kịch bản bổ sung: Lấy danh sách danh mục khả dụng
     * MỤC TIÊU: Đảm bảo hệ thống trả về các danh mục không trùng lặp.
     */
    @Test
    public void testGetAvailableCategories() {
        ProductCategory category = new ProductCategory();
        List<ProductCategory.CategorizedProduct> products = Arrays.asList(
            new ProductCategory.CategorizedProduct("Son MAC", "Son môi", 650000, 10),
            new ProductCategory.CategorizedProduct("Son Black Rouge", "Son môi", 150000, 20),
            new ProductCategory.CategorizedProduct("Nước hoa Chanel", "Nước hoa", 3500000, 5)
        );

        List<String> categories = category.getAvailableCategories(products);
        assertEquals(2, categories.size());
        assertTrue(categories.contains("Son môi"));
        assertTrue(categories.contains("Nước hoa"));
    }
}
