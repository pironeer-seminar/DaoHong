package com.example.orderapp;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.orderapp.domain.User;
import com.example.orderapp.dto.UserDto;
import com.example.orderapp.repository.UserRepository;
import com.example.orderapp.service.UserService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("새로운 유저를 등록하면, DB에 저장되어야 한다.")
    void createUserTest() {
        // given
        UserDto.Request request = new UserDto.Request();
        request.setName("Alice");
        request.setEmail("alice@example.com");

        // when
        Long savedUserId = userService.join(request);
        em.flush();
        em.clear();

        User found = userRepository.findById(savedUserId).orElse(null);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Alice");
        assertThat(found.getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    @DisplayName("전체 유저 목록을 조회할 수 있어야 한다.")
    void getUsersTest() {
        // given
        // 기존 데이터베이스에 있는 사용자 수 확인
        int initialUserCount = userService.findAll().size();
        
        UserDto.Request request1 = new UserDto.Request();
        request1.setName("Bob");
        request1.setEmail("bob@example.com");
        userService.join(request1);

        UserDto.Request request2 = new UserDto.Request();
        request2.setName("Charlie");
        request2.setEmail("charlie@example.com");
        userService.join(request2);
        em.flush();
        em.clear();

        // when
        List<UserDto.Response> userList = userService.findAll();

        // then
        assertThat(userList).hasSize(initialUserCount + 2);
        assertThat(userList)
                .extracting("name")
                .contains("Bob", "Charlie");
    }
} 