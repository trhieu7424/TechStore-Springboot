package com.example.baitap.repository;

import com.example.baitap.entity.Order;
import com.example.baitap.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Truy vấn cơ bản 
    List<Order> findByUser(User user);

    // JPQL
    // Tính tổng doanh thu của cửa hàng
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.status = 'COMPLETED'")
    Double calculateTotalRevenue();
    
    // Tìm các đơn hàng có giá trị lớn hơn X
    @Query("SELECT o FROM Order o WHERE o.totalPrice > :minPrice")
    List<Order> findHighValueOrders(Double minPrice);
}