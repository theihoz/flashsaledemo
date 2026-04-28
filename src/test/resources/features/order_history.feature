# language: vi
Tính năng: US8 - Lịch sử đơn hàng chi tiết
  Là khách hàng
  Tôi muốn xem lại lịch sử các đơn hàng Flash Sale đã mua
  Để theo dõi số tiền đã tiết kiệm được và trạng thái đơn hàng

  Kịch bản: 8.1 - Xem trạng thái đơn hàng Flash Sale chi tiết (Happy Path)
    Cho khách hàng đã đặt thành công đơn hàng trong đợt Flash Sale
    Khi khách hàng truy cập mục "Lịch sử đơn hàng"
    Thì hệ thống hiển thị đầy đủ tên sản phẩm, mức giá đã mua, số tiền tiết kiệm được và trạng thái "Đang đóng gói"

  Kịch bản: 8.2 - Khách hàng chưa có lịch sử mua hàng (Unhappy Path)
    Cho khách hàng mới đăng ký tài khoản và chưa mua hàng
    Khi khách hàng truy cập mục "Lịch sử đơn hàng"
    Thì hệ thống hiển thị thông báo "Bạn chưa có đơn hàng nào" và hiện nút "Khám phá deal Hot"
