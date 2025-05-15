package com.edu.qlda.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class JwtTokenUtil {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {}) // ✅ Không cần gọi .and() nữa
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll() // ✅ Cho phép truy cập login không cần auth
                        .anyRequest().authenticated() // Các request khác phải đăng nhập
                );
        return http.build();
    }
}