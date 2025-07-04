package com.chakak.config;

import com.chakak.service.CustomUserDetailsService;
import com.chakak.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final ApplicationContext applicationContext;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public OncePerRequestFilter jwtAuthenticationFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                    FilterChain filterChain) throws ServletException, IOException {

                String requestURI = request.getRequestURI();
                log.debug("JWT Filter processing: {}", requestURI);

                // 로그인, 회원가입, 정적 리소스 경로는 JWT 검증 스킵
                if (requestURI.equals("/login") || requestURI.equals("/register") || requestURI.startsWith("/css/")
                        || requestURI.startsWith("/js/") || requestURI.startsWith("/images/")
                        || requestURI.equals("/favicon.ico")) {
                    log.debug("Skipping JWT validation for: {}", requestURI);
                    filterChain.doFilter(request, response);
                    return;
                }

                final String token = getTokenFromRequest(request);
                log.debug("Extracted token: {}", token != null ? "present" : "null");

                if (token != null && jwtUtil.validateToken(token)) {
                    String username = jwtUtil.extractUsername(token);
                    log.debug("Token username: {}", username);

                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        try {
                            UserDetailsService userDetailsService = applicationContext
                                    .getBean(CustomUserDetailsService.class);
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                            log.debug("Loaded user details: {}", userDetails.getUsername());

                            if (jwtUtil.validateToken(token, userDetails)) {
                                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());
                                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                SecurityContextHolder.getContext().setAuthentication(authToken);
                                log.debug("Authentication set for user: {}", username);
                            }
                        } catch (Exception e) {
                            log.error("JWT authentication error: ", e);
                        }
                    }
                } else {
                    log.debug("No valid token found for request: {}", requestURI);
                }

                filterChain.doFilter(request, response);
            }

            private String getTokenFromRequest(HttpServletRequest request) {
                if (request.getCookies() != null) {
                    for (Cookie cookie : request.getCookies()) {
                        if ("token".equals(cookie.getName())) {
                            log.debug("Token found in cookie");
                            return cookie.getValue();
                        }
                    }
                }

                String bearerToken = request.getHeader("Authorization");
                if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                    log.debug("Token found in Authorization header");
                    return bearerToken.substring(7);
                }
                return null;
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/check-userid", "/check-email", "/css/**",
                                "/js/**", "/images/**", "/favicon.ico")
                        .permitAll()
                        .requestMatchers("/mypage", "/edit", "/withdraw").authenticated()
                        .requestMatchers("/api/reports/**", "/api/comments/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("token"));

        return http.build();

    }

}