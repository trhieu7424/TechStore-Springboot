package com.example.baitap.service;

import com.example.baitap.dto.ProductDto;
import com.example.baitap.entity.Product;
import com.example.baitap.repository.ProductRepository;
import com.example.baitap.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository; // Giả lập Database

    @InjectMocks
    private ProductServiceImpl productService; // Service cần test

    @Test
    void testGetAllProducts_ShouldReturnList() {
        // 1. Giả lập dữ liệu
        Product p1 = new Product(); p1.setId(1L); p1.setName("Laptop"); p1.setPrice(1000.0);
        Product p2 = new Product(); p2.setId(2L); p2.setName("Mouse"); p2.setPrice(50.0);
        
        Mockito.when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        // 2. Gọi hàm cần test
        List<ProductDto> result = productService.getAllProducts();

        // 3. Kiểm tra kết quả (Assert)
        Assertions.assertEquals(2, result.size()); // Phải trả về 2 món
        Assertions.assertEquals("Laptop", result.get(0).getName()); // Tên món đầu phải đúng
    }

    @Test
    void testGetProductById_ShouldReturnProduct() {
        // 1. Giả lập
        Product p = new Product();
        p.setId(99L);
        p.setName("Iphone 15");
        
        Mockito.when(productRepository.findById(99L)).thenReturn(Optional.of(p));

        // 2. Gọi hàm
        ProductDto result = productService.getProductById(99L);

        // 3. Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Iphone 15", result.getName());
    }
}