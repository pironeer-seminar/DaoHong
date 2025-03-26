package com.example.orderapp.service;

import com.example.orderapp.domain.User;
import com.example.orderapp.dto.UserDto;
import com.example.orderapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    // 사용자 등록
    @Transactional
    public Long join(UserDto.Request request) {
        User user = new User(request.getName(), request.getEmail());
        userRepository.save(user);
        return user.getId();
    }
    
    // 사용자 단건 조회
    public UserDto.Response findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id=" + id));
        return new UserDto.Response(user);
    }
    
    // 사용자 목록 조회
    public List<UserDto.Response> findAll() {
        return userRepository.findAll().stream()
                .map(UserDto.Response::new)
                .collect(Collectors.toList());
    }
} 