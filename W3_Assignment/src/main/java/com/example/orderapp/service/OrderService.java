package com.example.orderapp.service;

import com.example.orderapp.domain.Order;
import com.example.orderapp.domain.OrderItem;
import com.example.orderapp.domain.Product;
import com.example.orderapp.domain.User;
import com.example.orderapp.dto.OrderDto;
import com.example.orderapp.repository.OrderRepository;
import com.example.orderapp.repository.ProductRepository;
import com.example.orderapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    
    // 주문 생성
    @Transactional
    public Long order(OrderDto.Request request) {
        // 엔티티 조회
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        
        // 주문 상품 생성
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderDto.Request.OrderItemRequest item : request.getOrderItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
            
            // 주문 상품 생성 및 추가
            OrderItem orderItem = OrderItem.createOrderItem(
                    product, 
                    product.getPrice(), 
                    item.getQuantity()
            );
            orderItems.add(orderItem);
        }
        
        // 주문 생성
        Order order = Order.createOrder(user, orderItems);
        
        // 주문 저장
        orderRepository.save(order);
        
        return order.getId();
    }
    
    // 주문 단건 조회
    public OrderDto.Response findById(Long id) {
        Order order = orderRepository.findOrderWithUser(id);
        if (order == null) {
            throw new IllegalArgumentException("존재하지 않는 주문입니다. id=" + id);
        }
        return new OrderDto.Response(order);
    }
    
    // 사용자별 주문 목록 조회
    public List<OrderDto.ListResponse> findOrdersByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        
        return orderRepository.findByUser(user).stream()
                .map(OrderDto.ListResponse::new)
                .collect(Collectors.toList());
    }
    
    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
        
        order.cancel();
    }
} 