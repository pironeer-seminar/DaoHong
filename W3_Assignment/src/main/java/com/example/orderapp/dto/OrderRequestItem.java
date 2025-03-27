package com.example.orderapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class OrderRequestItem {
    private Long productId;
    private int quantity;
    
    public OrderRequestItem(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
} 