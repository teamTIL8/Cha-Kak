package com.chakak.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy; // 이 임포트가 추가되었는지 확인해주세요.


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ✨ 이 줄이 추가되었는지 확인해주세요! ✨
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users/me/**").authenticated() // 마이페이지 관련은 인증 필요
                .requestMatchers(HttpMethod.GET, "/api/reactions/*/count").permitAll() // 특정 GET 요청은 허용
                .requestMatchers("/api/reactions/**").authenticated() // 반응 생성/수정/삭제 등은 인증 필요
                .requestMatchers("/api/**").permitAll() // 그 외 /api 경로는 허용
                .anyRequest().authenticated() // 나머지 모든 요청은 인증 필요 (위의 permitAll 규칙들에 걸리지 않는 경우)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}