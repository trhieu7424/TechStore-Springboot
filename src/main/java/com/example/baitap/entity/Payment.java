package com.example.baitap.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    private LocalDateTime paymentDate;
    private Double amount;
    private String paymentMethod; // "CASH", "BANK_TRANSFER"
    private String status; // "SUCCESS", "FAILED"
}