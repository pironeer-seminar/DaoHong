package com.example.orderapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    
    @Column(nullable = false)
    private int orderPrice;
    
    @Column(nullable = false)
    private int quantity;
    
    // 생성 메소드
    public static OrderItem createOrderItem(Product product, int orderPrice, int quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setQuantity(quantity);
        
        // 재고 감소
        product.removeStock(quantity);
        
        return orderItem;
    }
    
    // 연관관계 메소드
    public void setOrder(Order order) {
        this.order = order;
    }
    
    private void setProduct(Product product) {
        this.product = product;
    }
    
    private void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }
    
    private void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    // 비즈니스 로직
    
    // 주문 취소
    public void cancel() {
        getProduct().addStock(quantity);
    }
    
    // 주문상품 전체 가격 조회
    public int getTotalPrice() {
        return getOrderPrice() * getQuantity();
    }
} 