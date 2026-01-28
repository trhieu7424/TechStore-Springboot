package com.example.baitap.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "orders")
public class Order extends BaseEntity {
    
    private Double totalPrice;
    private String status; 

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; 

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails; 
}