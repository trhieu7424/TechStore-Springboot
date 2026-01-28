package com.example.baitap.service.impl;

import com.example.baitap.entity.*;
import com.example.baitap.repository.*;
import com.example.baitap.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private PaymentRepository paymentRepository; 
    @Autowired private CartRepository cartRepository;

    // 1. Tạo đơn hàng từ nút "Mua ngay" (1 sản phẩm)
    @Override
    @Transactional
    public Order createOrder(User user, List<Long> productIds, String paymentMethod) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(0.0);
        order = orderRepository.save(order); 

        double totalAmount = 0;
        List<OrderDetail> details = new ArrayList<>();

        for (Long pId : productIds) {
            Product product = productRepository.findById(pId)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại: " + pId));
            
            if (product.getQuantity() < 1) {
                throw new RuntimeException("Sản phẩm đã hết hàng: " + product.getName());
            }

            product.setQuantity(product.getQuantity() - 1);
            productRepository.save(product);

            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(1);
            detail.setPrice(product.getPrice());
            
            details.add(detail);
            totalAmount += product.getPrice();
        }

        order.setOrderDetails(details);
        order.setTotalPrice(totalAmount);
        Order savedOrder = orderRepository.save(order);

        // Tạo thanh toán
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setAmount(totalAmount);
        payment.setPaymentMethod(paymentMethod); 
        payment.setStatus(paymentMethod.equals("BANKING") ? "COMPLETED" : "PENDING");
        paymentRepository.save(payment);

        return savedOrder;
    }

    // 2. Tạo đơn hàng từ Giỏ hàng (Nhiều sản phẩm)
    @Override
    @Transactional
    public Order createOrderFromCart(User user, String paymentMethod) {
        // Lấy giỏ hàng của user
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng trống"));
        
        if(cart.getItems().isEmpty()) {
            throw new RuntimeException("Giỏ hàng không có sản phẩm nào");
        }

        // Tạo Order
        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        
        List<OrderDetail> details = new ArrayList<>();
        double totalAmount = 0;

        // Duyệt từng món trong giỏ để chuyển sang đơn hàng
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            
            // Kiểm tra kho 
            if (product.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + product.getName() + " không đủ số lượng!");
            }
            
            // Trừ kho
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);

            // Tạo chi tiết đơn
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getPrice());
            
            details.add(detail);
            totalAmount += (item.getPrice() * item.getQuantity());
        }

        order.setOrderDetails(details);
        order.setTotalPrice(totalAmount);
        Order savedOrder = orderRepository.save(order);

        // Tạo Thanh toán
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setAmount(totalAmount);
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus(paymentMethod.equals("BANKING") ? "COMPLETED" : "PENDING");
        paymentRepository.save(payment);

        cart.getItems().clear();
        cartRepository.save(cart);

        return savedOrder;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"));
    }

    @Override
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }
}