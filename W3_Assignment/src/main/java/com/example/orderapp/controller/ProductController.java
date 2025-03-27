package com.example.orderapp.controller;

import com.example.orderapp.dto.ProductDto;
import com.example.orderapp.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    
    @PostMapping
    public ResponseEntity<Long> register(@RequestBody @Valid ProductDto.Request request) {
        Long id = productService.register(request);
        return ResponseEntity.created(URI.create("/api/products/" + id)).body(id);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto.Response> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }
    
    @GetMapping
    public ResponseEntity<List<ProductDto.Response>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }
} 