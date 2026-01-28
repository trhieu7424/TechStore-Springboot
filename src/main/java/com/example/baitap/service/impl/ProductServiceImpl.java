package com.example.baitap.service.impl;

import com.example.baitap.dto.ProductDto;
import com.example.baitap.entity.Category;
import com.example.baitap.entity.Product;
import com.example.baitap.mapper.ProductMapper;
import com.example.baitap.repository.CategoryRepository;
import com.example.baitap.repository.ProductRepository;
import com.example.baitap.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        Product product = ProductMapper.toEntity(productDto);

        product.setImageUrl(productDto.getImageUrl());

        if (productDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Category với ID: " + productDto.getCategoryId()));
            product.setCategory(category);
        }

        Product savedProduct = productRepository.save(product);

        return ProductMapper.toDto(savedProduct);
    }

        @Override
            public List<ProductDto> searchProducts(String keyword) {
                List<Product> entities = productRepository.findByNameContainingIgnoreCase(keyword);
                
                return entities.stream()
                        .map(ProductMapper::toDto)
                        .collect(Collectors.toList());
            }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) { 
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm id: " + id));

        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setQuantity(productDto.getQuantity());

        if (productDto.getImageUrl() != null && !productDto.getImageUrl().isEmpty()) {
            existingProduct.setImageUrl(productDto.getImageUrl());
        }

        if (productDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category không tồn tại"));
            existingProduct.setCategory(category);
        }

        Product updatedProduct = productRepository.save(existingProduct);
        return ProductMapper.toDto(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) { 
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Sản phẩm không tồn tại để xóa");
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getProductById(Long id) { 
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        return ProductMapper.toDto(product);
    }


    @Override
    public List<ProductDto> searchByName(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> findByCategoryAndPrice(String categoryName, Double minPrice) {
        return productRepository.findByCategoryNameAndPrice(categoryName, minPrice).stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }
}