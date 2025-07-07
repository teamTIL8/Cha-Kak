package com.chakak.config;

import com.chakak.filter.JwtAuthenticationFilter;
import com.chakak.service.AuthService;
import com.chakak.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final AuthService authService;
    

    public SecurityConfig(JwtUtil jwtUtil, @Lazy AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
            		 // 🔓 공개 접근 허용
                    .requestMatchers("/", "/login", "/signup", "/css/**", "/js/**", "/images/**").permitAll()
                    .requestMatchers("/report/main", "/report/detail/**").permitAll()  // 목록, 상세 페이지는 공개
                    .requestMatchers(HttpMethod.GET, "/api/reports/**").permitAll()    // 목록 조회용 API는 공개

                    // 🔒 인증 필요한 API
                    .requestMatchers("/my/**").authenticated()
                    .requestMatchers("/report/register", "/report/mypage", "/report/edit/**").authenticated()
                    .requestMatchers("/api/reactions/**").authenticated()
                    .requestMatchers("/api/comments/**").authenticated()
                    .requestMatchers("/api/reports/**").authenticated()
                    .requestMatchers("/api/reactions/**").authenticated()
                    .requestMatchers("/api/comments/**").authenticated()
                    .requestMatchers("/api/reports/**").authenticated()
                    .requestMatchers("/report/register", "/report/mypage", "/report/edit/**").authenticated()

                    // 🔒 그 외 모든 요청은 인증 필요
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, authService), UsernamePasswordAuthenticationFilter.class)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .logout(logout -> logout.logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("token"));

        return http.build();
    }


}
