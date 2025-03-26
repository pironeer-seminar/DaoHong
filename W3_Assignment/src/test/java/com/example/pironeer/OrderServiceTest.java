package com.example.pironeer;



import com.example.pironeer.domain.*;
import com.example.pironeer.repository.*;
import com.example.pironeer.service.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @PersistenceContext
    EntityManager em;
    private Long savedUserId;
    private Long savedProductId1;
    private Long savedProductId2;

    @BeforeEach
    void setUp() {
        // 유저 생성
        User user = new User("Tester", "tester@example.com");
        savedUserId = userService.createUser(user);

        // 상품 생성
        Product p1 = new Product("Keyboard", 30_000, 5);
        Product p2 = new Product("Mouse", 20_000, 5);
        savedProductId1 = productService.createProduct(p1);
        savedProductId2 = productService.createProduct(p2);
    }

    @Test
    @DisplayName("주문 생성 시 유저와 상품 재고가 정상적으로 처리되어야 한다.")
    void createOrderTest() {
        // given
        OrderRequestItem item1 = new OrderRequestItem(savedProductId1, 2); // 키보드 2개
        OrderRequestItem item2 = new OrderRequestItem(savedProductId2, 1); // 마우스 1개

        // when
        Long orderId = orderService.createOrder(savedUserId, List.of(item1, item2));
        em.flush();
        em.clear();
        // then
        Order foundOrder = orderRepository.findById(orderId).orElse(null);
        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getOrderItems()).hasSize(2);

        // 재고 감소 확인
        Product keyboard = productRepository.findById(savedProductId1).orElse(null);
        Product mouse = productRepository.findById(savedProductId2).orElse(null);

        assertThat(keyboard.getStockQuantity()).isEqualTo(3); // 5 -> 3
        assertThat(mouse.getStockQuantity()).isEqualTo(4);    // 5 -> 4
    }

    @Test
    @DisplayName("주문 생성 중 재고 부족 시 전체 작업이 롤백되어야 한다.")
    void createOrderRollbackTest() {
        // given
        OrderRequestItem item1 = new OrderRequestItem(savedProductId1, 6); // 키보드 6개 (재고 5개)
        OrderRequestItem item2 = new OrderRequestItem(savedProductId2, 2);

        // when
        assertThrows(IllegalStateException.class, () -> {
            orderService.createOrder(savedUserId, List.of(item1, item2));
        });

        // then
        List<Order> allOrders = orderRepository.findAll();
        assertThat(allOrders).isEmpty();

        Product keyboard = productRepository.findById(savedProductId1).orElse(null);
        assertThat(keyboard.getStockQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("특정 유저의 주문 목록을 조회할 수 있어야 한다.")
    void getUserOrdersTest() {
        // given
        OrderRequestItem item1 = new OrderRequestItem(savedProductId1, 1);
        orderService.createOrder(savedUserId, List.of(item1));

        OrderRequestItem item2 = new OrderRequestItem(savedProductId2, 2);
        orderService.createOrder(savedUserId, List.of(item2));

        em.flush();
        em.clear();
        // when
        List<Order> userOrders = orderService.getOrdersByUserId(savedUserId);

        // then
        assertThat(userOrders).hasSize(2);
    }
}
