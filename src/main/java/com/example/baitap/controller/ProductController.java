package com.example.baitap.controller;

import com.example.baitap.dto.ProductDto;
import com.example.baitap.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // Import xử lý file

import java.io.IOException;     // Import xử lý lỗi nhập xuất
import java.nio.file.Files;     // Import xử lý file
import java.nio.file.Path;      // Import đường dẫn
import java.nio.file.Paths;     // Import đường dẫn
import java.nio.file.StandardCopyOption; // Import copy file
import java.util.List;
import java.util.UUID;          // Import tạo tên file ngẫu nhiên

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/products") 
public class ProductController {

    @Autowired
    private ProductService productService;

    // 1. Lấy danh sách
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // 2. Lấy chi tiết (HATEOAS)
    @GetMapping("/{id}")
    public EntityModel<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        return EntityModel.of(product,
            linkTo(methodOn(ProductController.class).getProductById(id)).withSelfRel(),
            linkTo(methodOn(ProductController.class).getAllProducts()).withRel("products")
        );
    }

    // 3. Thêm mới sản phẩm (CÓ UPLOAD ẢNH)
    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<?> createProduct(
            @RequestParam("name") String name,
            @RequestParam("price") Double price,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        try {
            // Tạo DTO
            ProductDto productDto = new ProductDto();
            productDto.setName(name);
            productDto.setPrice(price);
            productDto.setQuantity(quantity);
            productDto.setCategoryId(categoryId);

            // Xử lý lưu file ảnh
            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads");
                
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                Files.copy(imageFile.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                productDto.setImageUrl("/uploads/" + fileName);
            } else {
                productDto.setImageUrl("https://placehold.co/400x300?text=No+Image");
            }

            return ResponseEntity.ok(productService.addProduct(productDto));

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Lỗi khi upload ảnh: " + e.getMessage());
        }
    }

    // 4. Cập nhật
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.updateProduct(id, productDto));
    }

    // 5. Xóa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // --- CÁC API TÌM KIẾM ---
    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchByName(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchByName(keyword));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProductDto>> filterByCatAndPrice(
            @RequestParam String category,
            @RequestParam Double minPrice) {
        return ResponseEntity.ok(productService.findByCategoryAndPrice(category, minPrice));
    }
}