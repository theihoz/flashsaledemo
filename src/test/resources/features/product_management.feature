# language: vi
Tính năng: US6 - Thêm mới và Xóa Sản phẩm khỏi Chiến dịch Flash Sale
  Là Quản lý cửa hàng
  Tôi muốn thêm mới hoặc xóa các sản phẩm trong chiến dịch Flash Sale
  Để linh hoạt điều chỉnh danh mục khuyến mãi theo thực tế tồn kho

  Kịch bản: 6.1 - Thêm sản phẩm mỹ phẩm hợp lệ (Happy Path)
    Cho Quản lý đang ở trang quản lý sản phẩm của chiến dịch "Lễ Hội Son Môi"
    Khi chọn sản phẩm "Son Kem Lì Black Rouge", đặt giá Flash Sale "150.000 VNĐ", số lượng "50" và nhấn "Thêm"
    Thì hệ thống thêm sản phẩm vào danh sách và báo "Thêm sản phẩm thành công"

  Kịch bản: 6.2 - Thêm sản phẩm đã tồn tại trong chiến dịch (Unhappy Path)
    Cho sản phẩm "Nước tẩy trang Bioderma" đã có trong danh mục Flash Sale
    Khi Quản lý cố gắng thêm lại "Nước tẩy trang Bioderma" vào cùng chiến dịch đó
    Thì hệ thống chặn thao tác và báo lỗi "Sản phẩm đã tồn tại trong chiến dịch Flash Sale này"

  Kịch bản: 6.3 - Xóa sản phẩm khỏi chiến dịch (Happy Path)
    Cho sản phẩm "Mặt nạ ngủ Laneige" đang nằm trong danh sách của chiến dịch Flash Sale
    Khi Quản lý nhấn nút "Xóa" cạnh sản phẩm và xác nhận
    Thì hệ thống loại bỏ sản phẩm khỏi danh sách chiến dịch và khôi phục giá bán lẻ thông thường
