package com.example.baitap.service;

import com.example.baitap.entity.Order;
import com.example.baitap.entity.User;
import java.util.List;

public interface OrderService {
    Order createOrder(User user, List<Long> productIds, String paymentMethod);
    List<Order> getAllOrders(); 
    Order updateOrderStatus(Long orderId, String status); 
    Order createOrderFromCart(User user, String paymentMethod);
    
    List<Order> getOrdersByUser(User user);
}