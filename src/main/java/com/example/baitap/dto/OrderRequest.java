package com.example.baitap.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private List<Long> productIds;
    private String paymentMethod; 
}