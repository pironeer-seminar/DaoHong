package com.example.pironeer;

import com.example.pironeer.domain.*;
import com.example.pironeer.repository.*;
import com.example.pironeer.service.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


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
        User user = new User("Alice", "alice@example.com");

        // when
        Long savedUserId = userService.createUser(user);
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
        userService.createUser(new User("Bob", "bob@example.com"));
        userService.createUser(new User("Charlie", "charlie@example.com"));
        em.flush();
        em.clear();

        // when
        List<User> userList = userService.getAllUsers();

        // then
        assertThat(userList).hasSize(2);
        assertThat(userList)
                .extracting("name")
                .containsExactlyInAnyOrder("Bob", "Charlie");
    }
}
