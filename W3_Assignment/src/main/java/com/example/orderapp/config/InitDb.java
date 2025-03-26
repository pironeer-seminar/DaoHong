package com.example.orderapp.config;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.orderapp.domain.Product;
import com.example.orderapp.domain.User;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Profile("local")
@Component
public class InitDb {

    private final InitService initService;
    
    // 명시적 생성자 추가
    public InitDb(InitService initService) {
        this.initService = initService;
    }

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    static class InitService {

        @PersistenceContext
        private EntityManager em;

        public void dbInit() {
            // 회원 생성
            User user1 = new User("user1", "user1@example.com");
            User user2 = new User("user2", "user2@example.com");
            em.persist(user1);
            em.persist(user2);

            // 상품 생성
            Product product1 = new Product("상품1", 10000, 100);
            Product product2 = new Product("상품2", 20000, 200);
            Product product3 = new Product("상품3", 30000, 300);
            em.persist(product1);
            em.persist(product2);
            em.persist(product3);
        }
    }
} 