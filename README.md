# 🛍️ TECHSTORE - HỆ THỐNG QUẢN LÝ BÁN HÀNG

Đồ án môn học: Lập trình Web Java (Spring Boot)
Giảng viên hướng dẫn: Nguyễn Minh Tuấn
Sinh viên thực hiện: Nguyễn Trọng Hiếu

## 🚀 Giới thiệu
TechStore là một trang web thương mại điện tử hoàn chỉnh, cho phép người dùng mua sắm các thiết bị công nghệ và quản trị viên quản lý hệ thống.

## ✨ Tính năng chính

### 👤 Dành cho Khách hàng (User)
- **Đăng ký / Đăng nhập** (Bảo mật với Spring Security).
- **Tìm kiếm sản phẩm** theo tên.
- **Xem chi tiết sản phẩm**.
- **Giỏ hàng:** Thêm, sửa, xóa, xem tổng tiền.
- **Thanh toán:**
  - Hỗ trợ COD (Thanh toán khi nhận hàng).
  - Hỗ trợ **QR Code** (VietQR) chuyển khoản nhanh.
- **Lịch sử đơn hàng:** Xem trạng thái đơn hàng đã mua.

### ⚙️ Dành cho Quản trị viên (Admin)
- **Dashboard:** Xem biểu đồ doanh thu 7 ngày gần nhất.
- **Quản lý Sản phẩm:** Thêm, Sửa, Xóa, Xem danh sách.
- **Quản lý Danh mục:** Phân loại sản phẩm.
- **Quản lý Đơn hàng:** Duyệt đơn (Pending -> Shipping -> Completed) hoặc Hủy đơn.
- **Quản lý Người dùng:** Xem và xóa tài khoản vi phạm.

## 🛠️ Công nghệ sử dụng
- **Backend:** Java 17, Spring Boot 3.x, Spring Security, JPA/Hibernate.
- **Frontend:** Thymeleaf, Bootstrap 5, JavaScript, Chart.js.
- **Database:** MySQL.
- **Build Tool:** Maven.

## 📦 Hướng dẫn cài đặt & Chạy

### 1. Chuẩn bị Database
- Mở MySQL/phpMyAdmin, tạo database tên: `quanlysanpham`.
- Cấu hình file `src/main/resources/application.properties`:
  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/quanlysanpham
  spring.datasource.username=root
  spring.datasource.password=  

### 2. Chạy ứng dụng
- Mở terminal tại thư mục dự án và chạy lệnh: .\mvnw clean spring-boot:run

### 3. Tài khoản mặc định
Sau khi chạy, hãy đăng ký 2 tài khoản sau để test:

1. Tài khoản Admin:

Đăng ký username: admin, email: admin@gmail.com

Vào Database, bảng users, sửa cột role của user này thành ADMIN.

2. Tài khoản Khách:

Đăng ký username: khach, email: khach@gmail.com (Mặc định là USER).