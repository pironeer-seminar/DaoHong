package com.example.orderapp.dto;

import com.example.orderapp.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class UserDto {
    
    @Data
    public static class Request {
        @NotBlank(message = "이름은 필수입니다.")
        private String name;
        
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String email;
    }
    
    @Data
    public static class Response {
        private Long id;
        private String name;
        private String email;
        
        public Response(User user) {
            this.id = user.getId();
            this.name = user.getName();
            this.email = user.getEmail();
        }
    }
} 