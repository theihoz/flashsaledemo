## 🟢 US1: Hiển thị trạng thái Flash Sale trên sản phẩm

### 1. Kiến trúc BCE (Boundary - Control - Entity)
> **Giải thích:** Khách hàng (Boundary) không trực tiếp đòi dữ liệu từ hệ thống tính toán (Entity), mà bắt buộc phải thông qua Bộ lọc (Control) làm nhiệm vụ bảo vệ logic.
```text
  [ BOUNDARY / GIAO DIỆN ]                ( CONTROL / XỬ LÝ )                 { ENTITY / LÕI NGHIỆP VỤ }
  
  ╔════════════════════╗               ┌────────────────────┐               ⟪──────────────────────⟫
  ║ MÀN HÌNH MUA HÀNG  ║ = Lấy SP ===> │ TRÌNH QUẢN LÝ DANH │ == Truy vấn =>│ DỮ LIỆU CHIẾN DỊCH & │
  ║ (CustomerBoundary) ║               │ MỤC (ProductCatalog)│               │ MỨC GIẢM (Campaign)  │
  ╚════════════════════╝ <== Trả về == └────────────────────┘ <== Kết quả ==⟪──────────────────────⟫
   Là nơi khách xem, nhấn                Là nhân viên chạy bàn                Là nhà bếp nấu công thức
```

### 2. Sơ đồ Kịch Bản (Sequence Diagram)
> **Kỹ thuật:** Luồng xử lý lấy thông tin giá từ Layer Boundary qua Control tới Entity.

```mermaid
sequenceDiagram
    autonumber
    actor Customer
    participant Boundary as Giao diện Khách - CustomerBoundary (Layer: Boundary)
    participant Control as Trình quản lý Sản phẩm - ProductCatalog (Layer: Control)
    participant Entity as Dữ liệu Chiến dịch - FlashSaleCampaign (Layer: Entity)

    Customer->>Boundary: getDisplayedPrice(campaign, productName)
    Boundary->>Control: getProductPrice(productName)
    Control->>Entity: isActive(currentTime)
    
        Note right of Control: Luồng 1: Flash Sale Đang mở
        Entity-->>Control: trả về [true, discountPercent]
        Control-->>Boundary: calculatedPrice (Giá đã giảm)
        Boundary-->>Customer: Hiển thị Giá Sale & Đồng hồ

        Note right of Control: Luồng 2: Sale đã hết hạn
        Entity-->>Control: trả về [false]
        Control-->>Boundary: originalPrice (Giá gốc)
        Boundary-->>Customer: Hiển thị Giá gốc & Ẩn đồng hồ
```

**Chi tiết luồng dữ liệu (Data Flow):**
*   **Dữ liệu vào (Input):** `campaign` (đối tượng chiến dịch), `productName` (tên sản phẩm cần xem).
*   **Tiến trình xử lý (Processing):**
    *   `CustomerBoundary` đóng vai trò chuyển tiếp yêu cầu, không giữ logic tính toán.
    *   `ProductCatalog` đóng vai trò "trung tâm tính toán": nhận `discountPercent` từ Entity để áp dụng công thức: `Price * (1 - discount/100)`.
    *   `FlashSaleCampaign` chỉ chịu trách nhiệm về trạng thái thời gian (Trạng thái logic: True/False).
*   **Kết quả trả về (Result):** Một chuỗi định dạng (String) chứa giá tiền cuối cùng hoặc thông tin lỗi để hiển thị trực tiếp lên UI.

### 3. Sơ đồ Trạng Thái (State Diagram)
> **Giải thích:** Vòng đời của một chiến dịch đi từ lúc ngủ đông đến lúc tự đào thải.
```mermaid
stateDiagram-v2
    [*] --> ChoKichHoat : Khởi tạo chiến dịch
    note left of ChoKichHoat
      Đồng hồ hiện tại
      SỚM HƠN thời điểm bắt đầu
    end note
    
    ChoKichHoat --> DangDienRa : Tới đúng thời điểm Sale
    
    DangDienRa --> DaKetThuc : Vượt qua thời điểm kết thúc
    note right of DaKetThuc
      Hệ thống tự khóa lại.
      Sản phẩm trở về giá gốc.
    end note
    
    DaKetThuc --> [*]
```

### 4. Thiết kế Cấu trúc file & Màn hình hiển thị
> Nơi các kỹ sư lưu trữ File và Giao diện phác thảo tương ứng.

```text
📁 CẤU TRÚC FOLDER THEO BCE:
 src/
  ╰─ main/java/com/cosmetics/flashsale/
      ├─ boundary/     ->  CustomerBoundary.ui (File thiết kế giao diện màu sắc)
      ├─ control/      ->  ProductCatalog.java (File điều chế kết nối)
      ╰─ entity/       ->  FlashSaleCampaign.java (File công thức tính toán thời gian)


🖥️ MÀN HÌNH WIREFRAME:
 ╭──────────────────────────────────────────────────────────╮
 │  < Quay lại               Chi tiết Son MAC Ruby Woo      │
 ├──────────────────────────────────────────────────────────┤
 │                                                          │
 │   ╭──────────────╮    [⚡ FLASH SALE ĐANG MỞ BÁN]       │
 │   │              │                                       │
 │   │      [📸]    │    Giá niêm yết: ~~1.500.000 VNĐ~~    │
 │   │   ẢNH CỦA    │    Giá k.mãi:    1.000.000 VNĐ        │
 │   │   SẢN PHẨM   │                                       │
 │   │              │   ╰ Đã tiết kiệm: 500.000 VNĐ ╯       │
 │   ╰──────────────╯                                       │
 │                                                          │
 │   ⏱ Đồng hồ hết hạn: 02:45:10      [ 🛒 THÊM VÀO GIỎ ]  │
 ╰──────────────────────────────────────────────────────────╯
```

---

## 🟢 US2: Xử lý tồn kho và thanh toán

### 1. Kiến trúc BCE
> **Giải thích:** Khi bấm giỏ hàng, thông tin truyền qua Bộ Check-out để đi xin cấp phép trừ kho ảo tại Cột Inventory (Nằm sâu và an toàn nhất).
```text
  [ BOUNDARY / GIAO DIỆN ]                ( CONTROL / XỬ LÝ )                 { ENTITY / LÕI NGHIỆP VỤ }
  
  ╔════════════════════╗               ┌────────────────────┐               ⟪──────────────────────⟫
  ║ GIAO DIỆN XÁC NHẬN ║ = Bấm mua ==> │ ĐIỀU PHỐI ĐƠN HÀNG │ === Lệnh ===> │ BỘ BẢO MẬT TỒN KHO   │
  ║ (CheckoutBoundary) ║               │   (OrderCheckout)  │               │ (FlashSaleInventory) │
  ╚════════════════════╝ <== T/C, Lỗi= └────────────────────┘ <== Trừ kho ==⟪──────────────────────⟫
```

### 2. Sơ đồ Kịch Bản (Sequence Diagram)
> **Kỹ thuật:** Luồng xử lý đặt hàng và trừ tồn kho an toàn (Thread-safe logic).

```mermaid
sequenceDiagram
    autonumber
    actor Customer
    participant Boundary as Giao diện Khách - CustomerBoundary (Layer: Boundary)
    participant Control as Điều phối Đơn hàng - OrderCheckout (Layer: Control)
    participant Entity as Bộ trữ Tồn kho - FlashSaleInventory (Layer: Entity)

    Customer->>Boundary: checkout(inventory, quantity)
    Boundary->>Control: processOrder(inventory, quantity)
    Control->>Entity: holdInventory(quantity)
    
        Note right of Control: Luồng 1: Còn hàng (Happy Path)
        Entity-->>Control: return true (Phép trừ Atomic)
        Control-->>Boundary: orderSuccess: true
        Boundary-->>Customer: Hiển thị "Thành công" & Hóa đơn

        Note right of Control: Luồng 2: Hết hàng (Unhappy Path)
        Entity-->>Control: throw IllegalStateException
        Control-->>Boundary: orderSuccess: false
        Boundary-->>Customer: Hiển thị Cảnh báo "Hết hàng"
```

**Chi tiết luồng dữ liệu (Data Flow):**
*   **Dữ liệu vào (Input):** `inventory` (đối tượng thực thể quản lý kho), `quantity` (số lượng khách đặt mua - phải > 0).
*   **Tiến trình xử lý (Processing):**
    *   `OrderCheckout` gọi phương thức `holdInventory(quantity)`.
    *   **Tại Entity:** Dữ liệu `availableQuantity` được bảo vệ bởi từ khóa `synchronized`. 
    *   **Logic:** Hệ thống kiểm tra điều kiện `if (availableQuantity >= quantity)`. Nếu thỏa mãn, thực hiện phép trừ trực tiếp vào vùng nhớ RAM (Database tạm thời).
*   **Đầu ra (Output):** Trả về trạng thái `true` để Control tiếp tục luồng tạo hóa đơn, hoặc ném lỗi ngay lập tức để ngắt giao dịch nếu kho không đủ.

### 3. Thiết kế Cấu trúc file & Màn hình hiển thị
```text
📁 CẤU TRÚC FOLDER:
 src/
  ╰─ main/java/.../
      ├─ boundary/     ->  CheckoutBoundary.html (Nút bấm thanh toán)
      ├─ control/      ->  OrderCheckout.java (Điều khiển giao tiếp giữ chỗ lệnh)
      ╰─ entity/       ->  FlashSaleInventory.java (Giữ khư khư thông tin Số Lượng)

🖥️ MÀN HÌNH WIREFRAME:
 ╭──────────────────────────────────────────────────────────╮
 │  Thanh toán giỏ hàng                                     │
 ├──────────────────────────────────────────────────────────┤
 │                                                          │
 │  Son MAC Ruby Woo                                        │
 │  SL: [ - ]  2  [ + ]   ................... 2.000.000 VNĐ │
 │                                                          │
 │  Tổng cộng (đã giảm):                    2.000.000 VNĐ   │
 │                                                          │
 │   ╔══════════════════════════════════════════════════╗   │
 │   ║ ⚠️ LỖI BÁO TỪ MÁY CHỦ:                           ║   │ <--- (Hiển thị mượt mà)
 │   ║    Rất tiếc! Số suất Flash Sale đã hết.          ║   │
 │   ╚══════════════════════════════════════════════════╝   │
 │                                                          │
 │                     [ XÁC NHẬN ĐẶT HÀNG ]                │
 ╰──────────────────────────────────────────────────────────╯
```

---

## 🟢 US3: Thiết lập chiến dịch (Dành cho Quản trị viên)

### 1. Kiến trúc BCE
> **Giải thích:** Quản lý làm việc với biểu mẫu, Controller truyền tới lõi, nếu % lớn hơn kịch trần, Entity sẽ "cạch mặt" từ chối lưu.
```text
  [ BOUNDARY / GIAO DIỆN ]                ( CONTROL / XỬ LÝ )                 { ENTITY / LÕI NGHIỆP VỤ }
  
  ╔════════════════════╗               ┌────────────────────┐               ⟪──────────────────────⟫
  ║ GIAO DIỆN ADMIN    ║ = Nhấn Lưu => │ BAN QUẢN LÝ TỰ ĐỘNG│ === Lệnh ===> │ BỘ NHIỆM VỤ SINH MỚI │
  ║   (AdminForm)      ║               │  (CampaignManager) │               │ (FlashSaleCampaign)  │
  ╚════════════════════╝ <== T/C, Lỗi= └────────────────────┘ <== Báo lỗi ==⟪──────────────────────⟫
```

### 2. Sơ đồ Kịch Bản (Sequence Diagram)
> **Kỹ thuật:** Luồng nghiệp vụ cấu hình chiến dịch mới kèm theo các ràng buộc bảo vệ biên lợi nhuận.

```mermaid
sequenceDiagram
    autonumber
    actor Admin
    participant Boundary as Giao diện Admin - AdminBoundary (Layer: Boundary)
    participant Control as Trình ghi Chiến dịch - CampaignManager (Layer: Control)
    participant Entity as Lõi quy định - FlashSaleCampaign (Layer: Entity)

    Admin->>Boundary: scheduleCampaign(start, end, discount)
    Boundary->>Control: createCampaign(start, end, discount)
    Control->>Entity: new FlashSaleCampaign(start, end, discount)
    
        Note right of Entity: Kiểm tra điều kiện ràng buộc
        alt discountPercent > 50% OR startTime >= endTime
            Entity-->>Control: ném IllegalArgumentException
            Control-->>Boundary: error: Thông báo lỗi chi tiết
            Boundary-->>Admin: Hiển thị Tooltip cảnh báo
        end

        Note right of Entity: Khi dữ liệu hợp lệ
        Entity-->>Control: Đối tượng được khởi tạo thành công
        Control-->>Boundary: success: Đã lên lịch
        Boundary-->>Admin: Hiển thị "Thành công"
```

**Chi tiết luồng dữ liệu (Data Flow):**
*   **Dữ liệu vào (Input):** `startTime`, `endTime` (LocalDateTime), `discountPercent` (double).
*   **Tiến trình xử lý (Processing):**
    *   Dữ liệu thô từ Giao diện được `AdminBoundary` đóng gói và gửi tới `CampaignManager`.
    *   **Chốt chặn cuối (Entity Constructor):** Đây là nơi dữ liệu bị kiểm soát gắt gao nhất. Nếu logic `end.isBefore(start)` hoặc `discount > 50.0` là đúng, tiến trình khởi tạo object sẽ bị hủy bỏ ngay lập tức (Fail-fast).
*   **Kết quả (Result):** Một thực thể `FlashSaleCampaign` sạch được nạp vào RAM, sẵn sàng để đồng bộ với `initial_data.json` nếu cần, đảm bảo hệ thống không bao giờ vận hành với thông số sai.

### 3. Thiết kế Cấu trúc file & Màn hình hiển thị
```text
📁 CẤU TRÚC FOLDER:
 src/
  ╰─ main/java/.../
      ├─ boundary/     ->  AdminFormBoundary.html (Bảng nhập giá trị)
      ├─ control/      ->  CampaignManager.java (Đóng vai thư ký lưu hồ sơ)
      ╰─ entity/       ->  FlashSaleCampaign.java (Các quy định cứng)

🖥️ MÀN HÌNH WIREFRAME:
 ╭──────────────────────────────────────────────────────────╮
 │  [ADMIN] Tạo mới chiến dịch Flash Sale                   │
 ├──────────────────────────────────────────────────────────┤
 │                                                          │
 │  📅 Ngày & Giờ bắt đầu:  [ 2026-04-20 08:00 ▾]         │
 │  📅 Ngày & Giờ kết thúc: [ 2026-04-20 12:00 ▾]         │
 │                                                          │
 │  🔥 Điền mức sale (%):                                   │
 │   ╭──────────────────────────────────╮                   │
 │   │ 60                               │ ❌ Không hộp lệ  │
 │   ╰──────────────────────────────────╯                   │
 │  [ LƯU CHIẾN DỊCH KHUYẾN MÃI ]                           │
 │                                                          │
 │  > _Mức giảm cho phép vượt rào kịch trần là 50%_         │
 ╰──────────────────────────────────────────────────────────╯
```

---

## 🟢 US4: Báo cáo hiệu quả thời gian thực

### 1. Kiến trúc BCE
> **Giải thích:** Admin ngồi xem Dashboard, hệ thống phải liên tục móc qua Analytics nhẩm tỷ lệ. Đề phòng máy nhẩm lỗi nếu Cấu hình chưa được nhập từ trước.
```text
  [ BOUNDARY / GIAO DIỆN ]                ( CONTROL / XỬ LÝ )                 { ENTITY / LÕI NGHIỆP VỤ }
  
  ╔════════════════════╗               ┌────────────────────┐               ⟪──────────────────────⟫
  ║ TẤM NỀN CHARTIST   ║ = Yêu cầu ==> │ CỘNG TÁC VIÊN ĐỌC  │ === Chọc ===> │ MÁY QUÉT KPI NHÀ KHO │
  ║(DashboardBoundary) ║               │ (DashboardControl)  │               │   (SaleAnalytics)    │
  ╚════════════════════╝ <== Đồ Thị == └────────────────────┘ <== Kết quả ==⟪──────────────────────⟫
```

### 2. Sơ đồ Kịch Bản (Sequence Diagram)
> **Kỹ thuật:** Luồng truy vấn báo cáo thời gian thực từ Entity Sales.

```mermaid
sequenceDiagram
    autonumber
    actor Admin
    participant Boundary as Giao diện Admin - AdminBoundary (Layer: Boundary)
    participant Control as Bộ máy Thống kê - DashboardController (Layer: Control)
    participant Entity as Máy quét số liệu - SaleAnalytics (Layer: Entity)

    Admin->>Boundary: getAnalyticsReport(analytics)
    Boundary->>Control: calculateReport(analytics)
    Control->>Entity: getSoldPercentage()
    
        Note right of Entity: Phòng thủ "Lỗi chia cho 0"
        alt initialTotalProduct == 0
            Entity-->>Control: throw IllegalStateException
            Control-->>Boundary: error: Thiếu cấu hình gốc
            Boundary-->>Admin: Cảnh báo "Cần nhập tồn kho ban đầu"
        end

        Note right of Entity: Dữ liệu hợp lệ
        Entity-->>Control: trả về soldPercentage (double)
        Control->>Entity: gọi getTotalRevenue()
        Entity-->>Control: trả về totalRevenue (double)
        Control-->>Boundary: mảng reportData [perc, rev]
        Boundary-->>Admin: Vẽ biểu đồ & Render KPI
```

**Chi tiết luồng dữ liệu (Data Flow):**
*   **Dữ liệu vào (Input):** `analytics` (Thực thể chứa các biến tích lũy `totalRevenue` và `soldQuantity`).
*   **Tiến trình xử lý (Processing):**
    *   `DashboardController` thu thập các giá trị nguyên bản từ Entity.
    *   **Xử lý số liệu:** Thực hiện phép chia tỷ lệ và bọc trong cơ chế kiểm soát lỗi `initialTotal == 0`.
    *   Dữ liệu được đóng gói vào một mảng `double[]` để tối ưu tốc độ truyền tải giữa các Layer.
*   **Kết quả (Result):** Trả về mảng dữ liệu định dạng chuẩn, giúp Boundary hiển thị doanh thu (ví dụ: 50,000,000) và tỷ lệ (ví dụ: 80%) mà không cần truy vấn lại Database.

### 3. Thiết kế Cấu trúc file & Màn hình hiển thị
```text
📁 CẤU TRÚC FOLDER:
 src/
  ╰─ main/java/.../
      ├─ boundary/     ->  DashboardBoundary.html (Biểu đồ)
      ├─ control/      ->  DashboardController.java (Tính toán đầu cuối)
      ╰─ entity/       ->  SaleAnalytics.java (Chứa các biến cộng dồn Total)

🖥️ MÀN HÌNH WIREFRAME:
 ╭──────────────────────────────────────────────────────────╮
 │  [ADMIN] Real-time Két Sắt Số Liệu (Live)                │
 ├──────────────────────────────────────────────────────────┤
 │                                                          │
 │   Tỷ lệ xả hàng (%):                                     │
 │   [██████████████████████             ] 80.0%            │
 │                                                          │
 │   Doanh thu đạt được đến trưa nay:                       │
 │   💰 50,000,000 VNĐ                                      │
 │                                                          │
 │   [ Cập Nhật Lại ]          Tình trạng: Mượt mà ✔        │
 ╰──────────────────────────────────────────────────────────╯
```

---

## 🟢 US5: Quản lý Sản phẩm và Combo Sale

### 1. Kiến trúc BCE
> **Giải thích:** Liên kết nhiều SP vào chung một khay mang tên là `Combo`.
```text
  [ BOUNDARY / GIAO DIỆN ]                ( CONTROL / XỬ LÝ )                 { ENTITY / LÕI NGHIỆP VỤ }
  
  ╔════════════════════╗               ┌────────────────────┐               ⟪──────────────────────⟫
  ║ GIAO DIỆN KẾT HỢP  ║ = Kéo thẻ ==> │ BAN QUẢN LÝ NHÂN SỰ│ === Nhét ===> │ LẴNG COMBO TỔNG HỢP  │
  ║  (ComboBoundary)   ║               │   (ComboManager)   │               │   (FlashSaleCombo)   │
  ╚════════════════════╝ <== Lưu OK == └────────────────────┘ <== Xét Duyệt=⟪──────────────────────⟫
```

### 2. Sơ đồ Kịch Bản (Sequence Diagram)
> **Kỹ thuật:** Luồng nghiệp vụ đóng gói Combo đa sản phẩm kèm bước kiểm tra tồn kho thành phần (validateStock).

```mermaid
sequenceDiagram
    autonumber
    actor Admin
    participant Boundary as Giao diện Admin - AdminBoundary (Layer: Boundary)
    participant Control as Ban quản lý Combo - ComboManager (Layer: Control)
    participant Entity as Gói Combo thực thể - FlashSaleCombo (Layer: Entity)

    Admin->>Boundary: createCombo(name, products, price, discount)
    Boundary->>Control: createCombo(name, products, price, discount)
    Control->>Entity: new FlashSaleCombo(name, products, price, discount)
    
    loop Kiểm tra từng sản phẩm (Validate Each)
        Entity->>Entity: verify product.availableQuantity > 0
    end
    
        Note right of Entity: Phát hiện SP hết hàng
        alt Component: Out of Stock
            Entity-->>Control: ném IllegalStateException
            Control-->>Boundary: error: SP thành phần lỗi
            Boundary-->>Admin: Đánh dấu đỏ SP lỗi trên giao diện
        end

        Note right of Entity: Tất cả SP hợp lệ
        Entity-->>Control: trả về đối tượng Combo
        Control-->>Boundary: success: Đã thêm vào danh sách
        Boundary-->>Admin: Hiển thị Combo trong Danh mục Sale
```

**Chi tiết luồng dữ liệu (Data Flow):**
*   **Dữ liệu vào (Input):** `List<FlashSaleInventory>` (Danh sách thực thể các sản phẩm thành phần), mức giảm giá `discount`.
*   **Tiến trình xử lý (Processing):**
    *   `ComboManager` đóng vai trò là "người thu gom", kết nối các sản phẩm lẻ lại với nhau.
    *   **Luồng kiểm tra (Deep Validation):** Entity thực hiện vòng lặp `for-each` để quét toàn bộ danh sách. 
    *   **Business Rule:** Nếu bất kỳ sản phẩm nào có `availableQuantity <= 0`, toàn bộ logic khởi tạo Combo sẽ bị hủy bỏ (Transactional consistency).
*   **Kết quả (Result):** Một thực thể `FlashSaleCombo` đa thành phần được đăng ký thành công, hoặc thông báo lỗi chỉ rõ sản phẩm nào đang cản trở việc tạo Combo.

### 3. Thiết kế Cấu trúc file & Màn hình hiển thị
```text
📁 CẤU TRÚC FOLDER:
 src/
  ╰─ main/java/.../
      ├─ boundary/     ->  ComboBoundary.html (Tích ô chọn vật phẩm)
      ├─ control/      ->  ComboManager.java (Đẩy các Box vào Kho Quản trị)
      ╰─ entity/       ->  FlashSaleCombo.java (Nhét tất cả vào 1 Entity tổ)

🖥️ MÀN HÌNH WIREFRAME:
 ╭──────────────────────────────────────────────────────────╮
 │  [ADMIN] Tạo Hộp Nhóm - Combo Sale                       │
 ├──────────────────────────────────────────────────────────┤
 │ Tên Combo đặt: [ Combo Mùa Hè 2026 .............. ]      │
 │                                                          │
 │ Mặt hàng đem vào gói Group:                              │
 │ [x] Son MAC Ruby Woo (Tồn: 10 cái)                       │
 │ [x] Nước hoa Chanel (Tồn: 0 cái)  <-- Vô tình tích nhầm  │
 │                                                          │
 │ Trị giá cắt máu (% giảm hộp): [ 30 ]                     │
 │                                                          │
 │ [ TIẾN HÀNH ĐÓNG GÓI! ]                                  │
 │                                                          │
 │ 🚫 Lỗi kẹt kho: Thằng "Nước hoa Chanel" rỗng rồi sếp ơi! │
 ╰──────────────────────────────────────────────────────────╯
```
