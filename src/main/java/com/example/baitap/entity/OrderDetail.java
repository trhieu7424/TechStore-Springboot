package com.example.baitap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "order_details")
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderDetail extends BaseEntity {
    
    private Integer quantity;
    private Double price; 

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}