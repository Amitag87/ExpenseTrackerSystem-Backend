package com.spendsmart.income.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())   // 🔥 CSRF disable - Postman ke liye zaroori
            .authorizeHttpRequests(auth -> auth
                    .anyRequest().permitAll()   // ✅ Saare requests allow - koi bhi block nahi
            );

        return http.build();
    }
}