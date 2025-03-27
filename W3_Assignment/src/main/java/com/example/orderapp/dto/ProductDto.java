package com.example.orderapp.dto;

import com.example.orderapp.domain.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class ProductDto {
    
    @Data
    public static class Request {
        @NotBlank(message = "상품명은 필수입니다.")
        private String name;
        
        @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
        private int price;
        
        @Min(value = 0, message = "재고는 0개 이상이어야 합니다.")
        private int stockQuantity;
    }
    
    @Data
    public static class Response {
        private Long id;
        private String name;
        private int price;
        private int stockQuantity;
        
        public Response(Product product) {
            this.id = product.getId();
            this.name = product.getName();
            this.price = product.getPrice();
            this.stockQuantity = product.getStockQuantity();
        }
    }
} 