package com.chakak.config;
import static org.springframework.security.config.Customizer.withDefaults;
import com.chakak.filter.JwtAuthenticationFilter;
import com.chakak.service.AuthService;
import com.chakak.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;



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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:8081");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*"); // GET, POST, PUT, DELETE, OPTIONS

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    
  


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http
    	.cors(withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth

            // 🔓 Permit All
            .requestMatchers("/", "/login", "/register", "/check-userid", "/check-email",
                             "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
            .requestMatchers(HttpMethod.GET,  "/report/detail/**").permitAll()
            .requestMatchers(HttpMethod.DELETE, "/api/comments/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/report/upload/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/report/list/reset").permitAll()
            .requestMatchers(HttpMethod.GET, "/report").permitAll()
            .requestMatchers("/report/list" , "/report/new" , "/report/edit/**").permitAll()
            
            // 🔒 Requires Authentication
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

            // 그 외는 인증 필요
            .anyRequest().authenticated()
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
