package com.example.baitap.controller;

import com.example.baitap.entity.User;
import com.example.baitap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Trả về file login.html
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register"; // Trả về file register.html
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email) { // 👇 ĐÃ THÊM NHẬN EMAIL

        // Kiểm tra trùng username
        if (userRepository.findByUsername(username).isPresent()) {
            return "redirect:/register?error=exist";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email); // 👇 LƯU EMAIL
        user.setRole("USER"); // Mặc định là khách hàng
        
        userRepository.save(user);

        return "redirect:/login?success";
    }
}