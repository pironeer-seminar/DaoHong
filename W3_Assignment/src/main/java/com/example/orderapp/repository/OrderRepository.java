package com.example.orderapp.repository;

import com.example.orderapp.domain.Order;
import com.example.orderapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
    @Query("select o from Order o join fetch o.user where o.id = :id")
    Order findOrderWithUser(@Param("id") Long id);
    
    List<Order> findByUser(User user);
} 