package com.cosmetics.flashsale.entity;

import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

public class FlashSaleCampaignTest {

    @Test
    public void testIsActive_HappyPath() {
        // Hệ thống giả lập vòng quay thời gian bắt đầu từ 1 giờ trước và sẽ kết thúc vào 1 giờ sau
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        FlashSaleCampaign campaign = new FlashSaleCampaign(start, end, 20.0);
        
        // Theo dõi xem hệ thống xử lý nội bộ có phản hồi thông tin chiến dịch này là "Đang diễn ra" (true) không
        assertTrue(campaign.isActive(LocalDateTime.now()));
    }

    @Test
    public void testIsInactive_PastCampaign() {
        // Cố tình thiết lập độ trễ đưa sự kiện lùi về quá khứ (đã kết thúc 1 giờ trước)
        LocalDateTime start = LocalDateTime.now().minusHours(2);
        LocalDateTime end = LocalDateTime.now().minusHours(1);
        FlashSaleCampaign campaign = new FlashSaleCampaign(start, end, 20.0);
        
        // Test kiểm chứng phần mềm có nhận diện phân loại được chiến dịch vào nhóm Đã Hết Hạn (false) hay không
        assertFalse(campaign.isActive(LocalDateTime.now()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiscountExceedsThreshold_UnhappyPath() {
        // Thử tạo một chiến dịch giảm mức quá trớn tới 60%. Theo chân hệ thống chờ văng ra biển báo lỗi Illegal Argument Cản Thiệp
        new FlashSaleCampaign(LocalDateTime.now(), LocalDateTime.now().plusHours(1), 60.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTime_UnhappyPath() {
        // Đùa dai với hệ thống: cài ngày kết thúc (now) diễn ra trước cả giờ bắt đầu (+1h). Máy tính sẽ báo lỗi chặn thao tác lú lẫn này.
        new FlashSaleCampaign(LocalDateTime.now().plusHours(1), LocalDateTime.now(), 20.0);
    }
}
