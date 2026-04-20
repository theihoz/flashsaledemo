# Tổng Quan: Sơ đồ Tuần Tự (Sequence Diagram) Toàn Dự Án

Sơ đồ dưới đây mô tả luồng hoạt động tổng thể của toàn bộ hệ thống Flash Sale từ lúc bắt đầu thiết lập chiến dịch cho đến khi khách hàng mua hàng và xem báo cáo tài chính. 

Hệ thống được thiết kế chặt chẽ theo kiến trúc **BCE (Boundary - Control - Entity)** nhằm tách biệt giao diện hiển thị, logic điều hướng và lõi nghiệp vụ.

## 1. Sơ đồ Tuần Tự Toàn Cảnh (Global Sequence Diagram)

```mermaid
sequenceDiagram
    autonumber
    
    actor Admin
    actor Customer

    box Giao diện (Layer: Boundary)
        participant AdminUI as Giao diện Admin
        participant CustomerUI as Giao diện Khách hàng
    end

    box Xử lý (Layer: Control)
        participant SetupCtrl as Quản lý Thiết lập (Campaign/Combo)
        participant ShopCtrl as Điều phối Mua sắm (Catalog/Order)
        participant DashCtrl as Xử lý Báo cáo (Dashboard)
    end

    box Lõi nghiệp vụ (Layer: Entity)
        participant E_Camp as Dữ liệu Chiến dịch & Combo
        participant E_Inv as Tồn kho (Inventory)
        participant E_Analyt as Máy quét Số liệu (Analytics)
    end

    %% Giai đoạn 1: Chuẩn bị
    rect rgb(230, 240, 255)
        Note right of Admin: GIAI ĐOẠN 1: THIẾT LẬP (Admin)
        Admin->>AdminUI: Cấu hình Khuyến mãi & Combo (US3, US5)
        AdminUI->>SetupCtrl: createCampaign() / createCombo()
        
        %% Check inventory for combo
        SetupCtrl->>E_Inv: Kiểm tra tồn kho thành phần
        E_Inv-->>SetupCtrl: Đủ điều kiện
        
        %% Save campaign/combo
        SetupCtrl->>E_Camp: Khởi tạo Entity & Lưu thông số
        E_Camp-->>SetupCtrl: Khởi tạo thành công (Kiểm tra kịch trần 50%)
        SetupCtrl-->>AdminUI: Thành công
        AdminUI-->>Admin: Hiển thị "Đã lên lịch"
    end

    %% Giai đoạn 2: Diễn ra chương trình
    rect rgb(240, 255, 240)
        Note left of Customer: GIAI ĐOẠN 2: CHẠY FLASH SALE (Customer)
        
        %% Xem giá sản phẩm
        Customer->>CustomerUI: Vào xem sản phẩm (US1)
        CustomerUI->>ShopCtrl: getProductPrice()
        ShopCtrl->>E_Camp: isActive(currentTime)
        E_Camp-->>ShopCtrl: true [Flash Sale mở]
        ShopCtrl-->>CustomerUI: calculatedPrice (Giá đã giảm)
        CustomerUI-->>Customer: Hiển thị Giá Sale & Đồng hồ

        %% Thanh toán
        Customer->>CustomerUI: Bấm mua hàng (US2)
        CustomerUI->>ShopCtrl: processOrder(quantity)
        ShopCtrl->>E_Inv: holdInventory(quantity)
        
        alt Trừ kho thành công
            E_Inv-->>ShopCtrl: true 
            ShopCtrl-->>CustomerUI: orderSuccess
            CustomerUI-->>Customer: Báo đặt hàng thành công
            %% Update analytics
            ShopCtrl-)E_Analyt: Cập nhật tăng Số lượng & Doanh thu
        else Hết hàng
            E_Inv-->>ShopCtrl: throw IllegalStateException
            ShopCtrl-->>CustomerUI: Hết hàng
            CustomerUI-->>Customer: Cảnh báo "Cháy hàng"
        end
    end

    %% Giai đoạn 3: Theo dõi thời gian thực
    rect rgb(255, 250, 230)
        Note right of Admin: GIAI ĐOẠN 3: BÁO CÁO (Admin)
        Admin->>AdminUI: Mở Dashboard xem tiến độ (US4)
        AdminUI->>DashCtrl: calculateReport()
        DashCtrl->>E_Analyt: getSoldPercentage() & getTotalRevenue()
        E_Analyt-->>DashCtrl: Trả về Số liệu KPI cộng dồn
        DashCtrl-->>AdminUI: Biểu đồ & Doanh thu
        AdminUI-->>Admin: Hiển thị Live Analytics
    end
```

## 2. Giải thích chi tiết các pha xử lý

Sơ đồ bao trọn 5 User Stories (US) chính và chia làm 3 giai đoạn độc lập:

### Giai đoạn 1: Chuẩn bị & Thiết lập (Setup Phase)
- **Actor:** Admin
- Quản trị viên (Admin) tạo chiến dịch (US3) và gom nhóm Combo sản phẩm (US5).
- Tầng **Control** (`SetupCtrl` đại diện `CampaignManager` / `ComboManager`) sẽ điều phối các thông tin từ Giao Diện.
- Tầng **Entity** (`E_Inv` và `E_Camp`) sẽ thực hiện validate các quy tắc cứng:
  - Phải kiểm tra hàng trong kho (`FlashSaleInventory`) xem có đủ tạo Combo không.
  - Phải xác minh Mức Sale không được vượt quá tối đa quy định (`FlashSaleCampaign`).

### Giai đoạn 2: Vận hành Flash Sale (Execution Phase)
- **Actor:** Customer
- Khách hàng (Customer) lướt xem hàng hóa. Tầng giao diện (`CustomerUI`) đòi giá từ Control (`ProductCatalog`), Control gọi xuống Entity (`FlashSaleCampaign`) kiểm tra thời gian Server xem có hiệu lực hay không (US1).
- Khi người mua nhấn **Thanh toán**, Control (`OrderCheckout`) ra lệnh cho Entity tồn kho (`FlashSaleInventory`) trừ trực tiếp vào RAM đồng bộ (Synchronized) để tránh gian lận / Over-booking (US2).
- Nếu kho nhận lệnh hợp lệ, lưu lượng sẽ được báo cho thực thể báo cáo (`SaleAnalytics`) để cộng dồn KPI.

### Giai đoạn 3: Báo cáo Thống kê (Analytics Phase)
- **Actor:** Admin
- Xảy ra theo thời gian thực (Real-time). Admin xem Dashboard, hệ thống liên tục lấy tham số doanh thu qua Controller (`DashboardController`) từ Entity bộ nhớ (`SaleAnalytics`) không qua khâu rườm rà (US4). Mọi biểu đồ được cập nhật sống động!

## 3. Kiến trúc Tổng quát BCE

* **Layer Boundary (Giao Diện/Biên):** Không chứa bất kỳ quy tắc tính toán nào. Chỉ chịu trách nhiệm tiếp nhận tương tác của người dùng, Form nhập liệu và hiển thị kết quả HTML/UI.
* **Layer Control (Điều Khiển/Môi giới):** Các lớp kết nối (Manager, Controller, Checkout). Làm cầu nối luân chuyển dữ liệu từ màn hình vào Lõi, điều phối nhiều Entity cùng lúc.
* **Layer Entity (Chủ Thể/Lõi Logic):** Chứa các quy định "Thép", ví dụ: khóa Thread-safe không cho mua trùng (Hold Inventory), ném lỗi khi thiếu số lượng cấu hình, hoặc chặn lưu khi giảm giá quá mớ. Nơi thuần túy bảo vệ tài sản doanh nghiệp.
