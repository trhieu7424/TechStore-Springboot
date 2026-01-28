package com.example.baitap.repository;

import com.example.baitap.entity.Cart;
import com.example.baitap.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}