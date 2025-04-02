package com.example.orderapp.dto;

import com.example.orderapp.domain.Order;
import com.example.orderapp.domain.OrderItem;
import com.example.orderapp.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDto {
    
    @Data
    public static class Request {
        private Long userId;
        private List<OrderItemRequest> orderItems;
        
        @Data
        public static class OrderItemRequest {
            private Long productId;
            private int quantity;
        }
    }
    
    @Data
    public static class Response {
        private Long id;
        private String userName;
        private List<OrderItemResponse> orderItems;
        private int totalPrice;
        private LocalDateTime orderDate;
        private OrderStatus status;
        
        public Response(Order order) {
            this.id = order.getId();
            this.userName = order.getUser().getName();
            this.orderItems = order.getOrderItems().stream()
                    .map(OrderItemResponse::new)
                    .collect(Collectors.toList());
            this.totalPrice = order.getTotalPrice();
            this.orderDate = order.getOrderDate();
            this.status = order.getStatus();
        }
        
        @Data
        public static class OrderItemResponse {
            private String productName;
            private int orderPrice;
            private int quantity;
            private int totalPrice;
            
            public OrderItemResponse(OrderItem orderItem) {
                this.productName = orderItem.getProduct().getName();
                this.orderPrice = orderItem.getOrderPrice();
                this.quantity = orderItem.getQuantity();
                this.totalPrice = orderItem.getTotalPrice();
            }
        }
    }
    
    @Data
    public static class ListResponse {
        private Long id;
        private LocalDateTime orderDate;
        private OrderStatus status;
        private int totalPrice;
        
        public ListResponse(Order order) {
            this.id = order.getId();
            this.orderDate = order.getOrderDate();
            this.status = order.getStatus();
            this.totalPrice = order.getTotalPrice();
        }
    }
} 