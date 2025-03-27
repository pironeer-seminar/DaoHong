package com.example.orderapp;

import com.example.orderapp.domain.Product;
import com.example.orderapp.dto.ProductDto;
import com.example.orderapp.repository.ProductRepository;
import com.example.orderapp.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void 상품_등록() throws Exception {
        // given
        ProductDto.Request request = new ProductDto.Request();
        request.setName("테스트 상품");
        request.setPrice(10000);
        request.setStockQuantity(100);

        // when
        Long productId = productService.register(request);

        // then
        Product product = productRepository.findById(productId).orElseThrow();
        assertEquals("테스트 상품", product.getName());
        assertEquals(10000, product.getPrice());
        assertEquals(100, product.getStockQuantity());
    }

    @Test
    public void 상품_단건_조회() throws Exception {
        // given
        Product product = createProduct("단건조회 상품", 20000, 50);

        // when
        ProductDto.Response response = productService.findById(product.getId());

        // then
        assertNotNull(response);
        assertEquals("단건조회 상품", response.getName());
        assertEquals(20000, response.getPrice());
        assertEquals(50, response.getStockQuantity());
    }

    @Test
    public void 상품_목록_조회() throws Exception {
        // given
        int initialCount = productService.findAll().size();
        createProduct("상품1", 10000, 10);
        createProduct("상품2", 20000, 20);
        createProduct("상품3", 30000, 30);

        // when
        List<ProductDto.Response> products = productService.findAll();

        // then
        assertEquals(initialCount + 3, products.size());
    }

    private Product createProduct(String name, int price, int stockQuantity) {
        Product product = new Product(name, price, stockQuantity);
        productRepository.save(product);
        return product;
    }
} 