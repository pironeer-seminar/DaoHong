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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("상품을 등록하면, DB에 저장되고 ID가 반환되어야 한다.")
    void createProductTest() {
        // given
        Product product = new Product("Laptop", 1_500_000, 10);

        // when
        Long productId = productService.createProduct(product);
        em.flush();
        em.clear();
        Product found = productRepository.findById(productId).orElse(null);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Laptop");
        assertThat(found.getPrice()).isEqualTo(1_500_000);
        assertThat(found.getStockQuantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("상품을 여러 개 등록 후, 전체 조회가 가능해야 한다.")
    void getAllProductsTest() {
        // given
        productService.createProduct(new Product("Phone", 1_000_000, 5));
        productService.createProduct(new Product("Tablet", 700_000, 3));

        // when
        em.flush();
        em.clear();
        List<Product> products = productService.getAllProducts();

        // then
        assertThat(products).hasSize(2);
        assertThat(products)
                .extracting("name")
                .containsExactlyInAnyOrder("Phone", "Tablet");
    }

    @Test
    @DisplayName("재고가 부족할 경우 예외가 발생해야 한다.")
    void decreaseStockFailTest() {
        // given
        Product product = new Product("Watch", 300_000, 2);
        Long productId = productService.createProduct(product);

        // when
        assertThrows(IllegalStateException.class, () -> {
            productService.decreaseStock(productId, 3);
        });

        // then
        // 예외 발생 후 stockQuantity는 여전히 2
        Product found = productRepository.findById(productId).orElse(null);
        assertThat(found.getStockQuantity()).isEqualTo(2);
    }
}
