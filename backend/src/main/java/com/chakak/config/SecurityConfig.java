package com.chakak.config;

import static org.springframework.security.config.Customizer.withDefaults;
import com.chakak.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@RequiredArgsConstructor  // final 필드 주입 위한 롬복 어노테이션
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

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
        http
            .cors(withDefaults())  // CORS 설정 활성화
            .csrf(AbstractHttpConfigurer::disable)  // CSRF 비활성화
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 세션 사용 안 함 (JWT 방식)
            .authorizeHttpRequests(auth -> auth
                
                .requestMatchers("/", "/login", "/register", "/check-userid", "/check-email",
                                 "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                .requestMatchers(HttpMethod.GET, "/report/detail/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/comments/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/report/upload/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/report/list/reset").permitAll()
                .requestMatchers(HttpMethod.GET, "/report").permitAll()
                .requestMatchers("/report/list", "/report/new", "/report/edit/**").permitAll()

                
                .requestMatchers(HttpMethod.POST, "/api/comments").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/comments/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/comments/users/me").authenticated()
                .requestMatchers(HttpMethod.POST, "/report").authenticated()
                .requestMatchers(HttpMethod.PUT, "/report/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/report/**").authenticated()
                .requestMatchers("/my/reports-reaction").authenticated()
                .requestMatchers(HttpMethod.GET, "/report/my").authenticated()
                .requestMatchers(HttpMethod.GET, "/reactions/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/reactions/**").authenticated()
                .requestMatchers("/my/reports", "/my/comments", "/my/likes", "/my/dislikes", "/report/*").authenticated()
                .requestMatchers(HttpMethod.GET, "/users/me/likes", "/users/me/dislikes").authenticated()

                
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)  // JWT 필터 등록
            .formLogin(AbstractHttpConfigurer::disable)  
            .httpBasic(AbstractHttpConfigurer::disable) 
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("token")
            );

        return http.build();
    }
}
