package com.example.orderapp.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users") // 'user'는 일부 DB에서 예약어이므로 'users'로 설정
@Getter
@NoArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();
    
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
} 