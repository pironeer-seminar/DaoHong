package com.example.orderapp.controller;

import com.example.orderapp.dto.UserDto;
import com.example.orderapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @PostMapping
    public ResponseEntity<Long> register(@RequestBody @Valid UserDto.Request request) {
        Long id = userService.join(request);
        return ResponseEntity.created(URI.create("/api/users/" + id)).body(id);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDto.Response> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }
    
    @GetMapping
    public ResponseEntity<List<UserDto.Response>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }
} 