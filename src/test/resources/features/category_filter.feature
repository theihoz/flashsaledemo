# language: vi
Tính năng: US7 - Phân loại sản phẩm theo danh mục
  Là khách hàng
  Tôi muốn lọc sản phẩm theo danh mục mỹ phẩm
  Để nhanh chóng tìm thấy sản phẩm yêu thích trong đợt Sale

  Kịch bản: 7.1 - Lọc danh mục mỹ phẩm thành công (Happy Path)
    Cho khách hàng đang ở trang Flash Sale tổng hợp
    Khi khách hàng chọn bộ lọc danh mục "Son môi"
    Thì hệ thống tải lại danh sách và chỉ hiển thị các sản phẩm thuộc danh mục "Son môi" đang có giá Flash Sale

  Kịch bản: 7.2 - Danh mục không có sản phẩm Flash Sale (Unhappy Path)
    Cho đợt sale hiện tại không có sản phẩm thuộc danh mục "Nước hoa"
    Khi khách hàng chọn bộ lọc "Nước hoa"
    Thì hệ thống hiển thị thông báo "Rất tiếc, chưa có sản phẩm nào trong danh mục này đang giảm giá" và đề xuất danh mục "Bán chạy nhất"
