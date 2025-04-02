package com.example.orderapp.controller;

import com.example.orderapp.dto.OrderDto;
import com.example.orderapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping
    public ResponseEntity<Long> order(@RequestBody OrderDto.Request request) {
        Long id = orderService.order(request);
        return ResponseEntity.created(URI.create("/api/orders/" + id)).body(id);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto.Response> findById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto.ListResponse>> findByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.findOrdersByUser(userId));
    }
    
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok().build();
    }
} 