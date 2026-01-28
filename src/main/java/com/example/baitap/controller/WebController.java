package com.example.baitap.controller;

import com.example.baitap.dto.ProductDto; 
import com.example.baitap.entity.Order;
import com.example.baitap.entity.User;
import com.example.baitap.repository.UserRepository;
import com.example.baitap.service.OrderService;
import com.example.baitap.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WebController {

    @Autowired private ProductService productService;
    @Autowired private OrderService orderService;
    @Autowired private UserRepository userRepository;

    // 1. Trang chủ (Có tìm kiếm)
    @GetMapping("/")
    public String home(@RequestParam(required = false) String keyword, Model model) {
        List<ProductDto> products;

        if (keyword != null && !keyword.isEmpty()) {
            products = productService.searchProducts(keyword);
        } else {
            products = productService.getAllProducts();
        }

        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        return "home";
    }

    // 2. Trang Admin (Dashboard xịn)
    @GetMapping("/admin")
    public String adminPage(Model model) {
        // Lấy danh sách sản phẩm (Dùng DTO)
        List<ProductDto> products = productService.getAllProducts();
        
        // Lấy danh sách đơn hàng (Dùng Entity)
        List<Order> orders = orderService.getAllOrders();
        
        // Lấy danh sách user (Dùng Entity)
        List<User> users = userRepository.findAll();

        // 👇 TÍNH TỔNG DOANH THU 
        double totalRevenue = 0;
        for (Order o : orders) {
            if (o.getStatus() != null && (o.getStatus().equals("COMPLETED") || o.getStatus().equals("SHIPPING"))) {
                totalRevenue += o.getTotalPrice();
            }
        }

        model.addAttribute("products", products);
        model.addAttribute("orders", orders);
        model.addAttribute("users", users);
        model.addAttribute("totalRevenue", totalRevenue); 

        return "admin";
    }

    // 3. Trang chi tiết sản phẩm
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "product_detail";
    }

    // 4. Trang thanh toán (Checkout Mua ngay)
    @GetMapping("/checkout")
    public String checkoutPage(@RequestParam Long productId, Model model) {
        model.addAttribute("product", productService.getProductById(productId));
        return "checkout";
    }

    // 5. Trang lịch sử đơn hàng
    @GetMapping("/history")
    public String historyPage() {
        return "history"; 
    }
}