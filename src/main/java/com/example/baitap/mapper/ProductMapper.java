package com.example.baitap.mapper;

import com.example.baitap.dto.ProductDto;
import com.example.baitap.entity.Product;

public class ProductMapper {

    public static ProductDto toDto(Product product) {
        if (product == null) return null;

        return ProductDto.builder()
                .id(product.getId()) 
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                
                // 👇 THÊM 1: Map đường dẫn ảnh để hiện ra web
                .imageUrl(product.getImageUrl()) 

                // 👇 Map ID để dùng cho logic backend
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)

                // 👇 THÊM 2 (QUAN TRỌNG): Map cả object Category để sửa lỗi 500
                .category(product.getCategory()) 
                .build();
    }

    public static Product toEntity(ProductDto dto) {
        if (dto == null) return null;

        Product product = new Product();
        product.setId(dto.getId()); 
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        
        // 👇 THÊM 3: Map đường dẫn ảnh để lưu xuống DB
        product.setImageUrl(dto.getImageUrl());
        
        return product;
    }
}