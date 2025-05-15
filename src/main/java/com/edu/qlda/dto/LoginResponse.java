package com.edu.qlda.dto;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
//    private String status;
//    private String message;
//    private AuthData data;
//
//    @Data
//    public static class AuthData {
//        private String accessToken;
//        private String refreshToken;
//        private long expiresIn;
//        private String tokenType;
//        private UserInfo user;
//    }
//
//    @Data
//    public static class UserInfo {
//        private Long id;
//        private String username;
//        private String email;
//        private String fullName;
//        private List<String> roles;
//        private String avatarUrl;
//    }
}
