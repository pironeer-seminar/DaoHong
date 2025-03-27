package com.example.orderapp.service;

import com.example.orderapp.domain.Product;
import com.example.orderapp.dto.ProductDto;
import com.example.orderapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    
    // 상품 등록
    @Transactional
    public Long register(ProductDto.Request request) {
        Product product = new Product(
                request.getName(),
                request.getPrice(),
                request.getStockQuantity()
        );
        productRepository.save(product);
        return product.getId();
    }
    
    // 상품 단건 조회
    public ProductDto.Response findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다. id=" + id));
        return new ProductDto.Response(product);
    }
    
    // 상품 목록 조회
    public List<ProductDto.Response> findAll() {
        return productRepository.findAll().stream()
                .map(ProductDto.Response::new)
                .collect(Collectors.toList());
    }
} 