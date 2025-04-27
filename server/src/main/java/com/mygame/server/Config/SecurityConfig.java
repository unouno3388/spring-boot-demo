package com.mygame.server.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 禁用CSRF保护
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll() // 允许所有请求
                .anyRequest().authenticated()
            );
        return http.build();
    }
}

