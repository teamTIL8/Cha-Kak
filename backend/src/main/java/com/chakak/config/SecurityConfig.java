package com.chakak.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 비활성화
            .authorizeHttpRequests(auth -> auth
                //.requestMatchers("/api/users/me/**").authenticated()  //이것은 url이 인증된 사용자만 접근 가능하도록 되어있음
                .requestMatchers("/api/**").permitAll() // 이거 임시방편용..
                .requestMatchers("/api/users/me/comments").permitAll()
                .anyRequest().authenticated()               // 나머지 요청은 인증 필요
            );

        return http.build();
    }
}