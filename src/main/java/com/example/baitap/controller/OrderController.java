package com.example.baitap.controller;

import com.example.baitap.dto.OrderRequest;
import com.example.baitap.entity.Order;
import com.example.baitap.entity.User;
import com.example.baitap.repository.UserRepository;
import com.example.baitap.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    // API Đặt hàng (Đã nâng cấp để nhận phương thức thanh toán)
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order newOrder = orderService.createOrder(user, request.getProductIds(), request.getPaymentMethod());
        return ResponseEntity.ok(newOrder);
    }

    // API Xem lịch sử đơn hàng
    @GetMapping
    public ResponseEntity<List<Order>> myOrders() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        return ResponseEntity.ok(orderService.getOrdersByUser(user));
    }

    @PostMapping("/from-cart")
    public ResponseEntity<?> createOrderFromCart(@RequestBody java.util.Map<String, String> payload) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        
        String paymentMethod = payload.get("paymentMethod");
        
        try {
            Order order = orderService.createOrderFromCart(user, paymentMethod);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}