package com.example.baitap.controller;

import com.example.baitap.entity.*;
import com.example.baitap.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class CartController {

    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;

    // 1. Xem trang giỏ hàng
    @GetMapping("/cart")
    public String viewCart(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username.equals("anonymousUser")) return "redirect:/login";
        
        User user = userRepository.findByUsername(username).orElseThrow();
        Cart cart = cartRepository.findByUser(user).orElse(new Cart());
        
        model.addAttribute("cart", cart);
        return "cart";
    }

    // 2. API: Thêm vào giỏ hàng
    @PostMapping("/api/cart/add")
    @ResponseBody
    public ResponseEntity<?> addToCart(@RequestParam Long productId, @RequestParam Integer quantity) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username.equals("anonymousUser")) return ResponseEntity.status(401).body("Vui lòng đăng nhập");

        User user = userRepository.findByUsername(username).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();

        Cart cart = cartRepository.findByUser(user).orElse(new Cart());
        if(cart.getUser() == null) {
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if(existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setPrice(product.getPrice());
            cartItemRepository.save(newItem);
        }

        return ResponseEntity.ok("Đã thêm vào giỏ");
    }

    // 3. API: Xóa khỏi giỏ
    @DeleteMapping("/api/cart/{itemId}")
    @ResponseBody
    public ResponseEntity<?> removeFromCart(@PathVariable Long itemId) {
        cartItemRepository.deleteById(itemId);
        return ResponseEntity.ok("Đã xóa");
    }
}