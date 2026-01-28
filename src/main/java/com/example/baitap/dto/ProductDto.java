package com.example.baitap.dto;

import com.example.baitap.entity.Category;
import lombok.AllArgsConstructor; 
import lombok.Builder;          
import lombok.Data;
import lombok.NoArgsConstructor; 

@Data
@Builder          
@NoArgsConstructor 
@AllArgsConstructor 
public class ProductDto {
    private Long id;
    private String name;
    private Double price;
    private Integer quantity;
    private String description;
    private String imageUrl;
    
    private Long categoryId; 
    private Category category; 
}