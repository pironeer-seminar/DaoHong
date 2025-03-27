package com.example.orderapp;

import com.example.orderapp.domain.Order;
import com.example.orderapp.domain.OrderStatus;
import com.example.orderapp.domain.Product;
import com.example.orderapp.domain.User;
import com.example.orderapp.dto.OrderDto;
import com.example.orderapp.repository.OrderRepository;
import com.example.orderapp.repository.ProductRepository;
import com.example.orderapp.repository.UserRepository;
import com.example.orderapp.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void 상품주문() throws Exception {
        // given
        User user = createUser("test@example.com", "testuser");
        Product product = createProduct("상품명", 10000, 10);

        int orderCount = 2;

        // when
        OrderDto.Request.OrderItemRequest itemRequest = new OrderDto.Request.OrderItemRequest();
        itemRequest.setProductId(product.getId());
        itemRequest.setQuantity(orderCount);
        
        List<OrderDto.Request.OrderItemRequest> orderItems = new ArrayList<>();
        orderItems.add(itemRequest);
        
        OrderDto.Request request = new OrderDto.Request();
        request.setUserId(user.getId());
        request.setOrderItems(orderItems);
        
        Long orderId = orderService.order(request);

        // then
        Order getOrder = orderRepository.findById(orderId).orElseThrow();
        assertEquals(OrderStatus.ORDERED, getOrder.getStatus(), "주문 시 상태는 ORDERED");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다");
        assertEquals(10000 * orderCount, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다");
        assertEquals(8, product.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다");
    }

    @Test
    public void 주문_취소() throws Exception {
        // given
        User user = createUser("cancel@example.com", "canceluser");
        Product product = createProduct("취소상품", 10000, 10);

        int orderCount = 2;

        OrderDto.Request.OrderItemRequest itemRequest = new OrderDto.Request.OrderItemRequest();
        itemRequest.setProductId(product.getId());
        itemRequest.setQuantity(orderCount);
        
        List<OrderDto.Request.OrderItemRequest> orderItems = new ArrayList<>();
        orderItems.add(itemRequest);
        
        OrderDto.Request request = new OrderDto.Request();
        request.setUserId(user.getId());
        request.setOrderItems(orderItems);
        
        Long orderId = orderService.order(request);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findById(orderId).orElseThrow();
        assertEquals(OrderStatus.CANCELED, getOrder.getStatus(), "주문 취소 시 상태는 CANCELED이다");
        assertEquals(10, product.getStockQuantity(), "주문이 취소된 상품은 그만큼 재고가 증가해야 한다");
    }

    @Test
    public void 주문_조회() throws Exception {
        // given
        User user = createUser("find@example.com", "finduser");
        Product product = createProduct("조회상품", 10000, 10);

        int orderCount = 2;

        OrderDto.Request.OrderItemRequest itemRequest = new OrderDto.Request.OrderItemRequest();
        itemRequest.setProductId(product.getId());
        itemRequest.setQuantity(orderCount);
        
        List<OrderDto.Request.OrderItemRequest> orderItems = new ArrayList<>();
        orderItems.add(itemRequest);
        
        OrderDto.Request request = new OrderDto.Request();
        request.setUserId(user.getId());
        request.setOrderItems(orderItems);
        
        Long orderId = orderService.order(request);

        // when
        OrderDto.Response response = orderService.findById(orderId);

        // then
        assertEquals(user.getName(), response.getUserName(), "주문한 유저 정보가 정확해야 한다");
        assertEquals(OrderStatus.ORDERED, response.getStatus(), "주문 상태가 정확해야 한다");
        assertEquals(1, response.getOrderItems().size(), "주문 상품 개수가 정확해야 한다");
    }

    private User createUser(String email, String name) {
        User user = new User(email, name);
        userRepository.save(user);
        return user;
    }

    private Product createProduct(String name, int price, int stockQuantity) {
        Product product = new Product(name, price, stockQuantity);
        productRepository.save(product);
        return product;
    }
} 