package com.example.baitap.service;

import com.example.baitap.dto.ProductDto;
// import com.example.baitap.entity.Product;

import java.util.List;

public interface ProductService {
    
    ProductDto addProduct(ProductDto productDto);

    ProductDto updateProduct(Long id, ProductDto productDto);

    void deleteProduct(Long id);

    List<ProductDto> getAllProducts();

    ProductDto getProductById(Long id);
    
    List<ProductDto> searchByName(String keyword);
    
    List<ProductDto> findByCategoryAndPrice(String categoryName, Double minPrice);

   List<ProductDto> searchProducts(String keyword);
}