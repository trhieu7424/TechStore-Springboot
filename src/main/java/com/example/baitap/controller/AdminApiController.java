package com.example.baitap.controller;

import com.example.baitap.entity.Order; 
import com.example.baitap.entity.User;
import com.example.baitap.repository.UserRepository;
import com.example.baitap.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    @Autowired private OrderService orderService;
    @Autowired private UserRepository userRepository;

    // --- QUẢN LÝ ĐƠN HÀNG ---
    @PutMapping("/orders/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String status = payload.get("status");
        orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok("Cập nhật trạng thái thành công");
    }

    // --- QUẢN LÝ USER ---
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok("Xóa user thành công");
    }

    // --- THỐNG KÊ DOANH THU (MỚI) ---
    @GetMapping("/revenue-stats")
    public ResponseEntity<?> getRevenueStats() {
        // Lấy tất cả đơn hàng
        List<Order> orders = orderService.getAllOrders();

        // Map để lưu: Ngày -> Tổng tiền
        Map<String, Double> revenueMap = new LinkedHashMap<>();

        // Khởi tạo 7 ngày gần nhất với giá trị 0
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            revenueMap.put(date.toString(), 0.0);
        }

        // Duyệt đơn hàng và cộng dồn tiền
        for (Order order : orders) {
            // Chỉ tính đơn đã hoàn thành hoặc đang giao
            if (order.getStatus().equals("COMPLETED") || order.getStatus().equals("SHIPPING")) {
                String orderDate = order.getCreatedAt().toLocalDate().toString();
                
                // Nếu ngày đó nằm trong 7 ngày gần nhất thì cộng tiền
                if (revenueMap.containsKey(orderDate)) {
                    revenueMap.put(orderDate, revenueMap.get(orderDate) + order.getTotalPrice());
                }
            }
        }

        return ResponseEntity.ok(revenueMap);
    }
}