<<<<<<< HEAD
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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users/me/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/reactions/*/count").permitAll()
                .requestMatchers("/api/reactions/**").authenticated()
                .requestMatchers("/api/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

// 폼 로그인 기반 (세션 기반 ) 코드 일단 보류
/*package com.chakak.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (API 개발 시 유용, 실제 서비스에서는 활성화 고려)
				.authorizeHttpRequests(authorize -> authorize.requestMatchers(new AntPathRequestMatcher("/"),
						new AntPathRequestMatcher("/signup"), new AntPathRequestMatcher("/login"),
						new AntPathRequestMatcher("/css/**"), new AntPathRequestMatcher("/js/**"),
						new AntPathRequestMatcher("/images/**"), new AntPathRequestMatcher("/api/auth/signup")
				).permitAll()
						.anyRequest().authenticated() // 그 외 모든 요청 (포함: /home, /mypage, /edit-profile, /api/auth/me,
														// /api/auth/update)은 인증된 사용자만 접근 허용
				).formLogin(form -> form.loginPage("/login") // 커스텀 로그인 페이지 경로
						.loginProcessingUrl("/perform_login") // 로그인 폼이 제출될 URL (Spring Security가 처리)
						.defaultSuccessUrl("/home", true) // 로그인 성공 시 이동할 기본 페이지
						.failureUrl("/login?error=true") // 로그인 실패 시 이동할 페이지
						.usernameParameter("userId") // 사용자 ID로 사용할 파라미터 이름 (기본값: username)
						.passwordParameter("password") // 비밀번호로 사용할 파라미터 이름 (기본값: password)
				).logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // 로그아웃 요청 URL
						.logoutSuccessUrl("/login?logout=true") // 로그아웃 성공 시 이동할 페이지
						.invalidateHttpSession(true) // 세션 무효화
						.deleteCookies("JSESSIONID") // 쿠키 삭제
				);
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		// 비밀번호 암호화를 위한 BCryptPasswordEncoder 빈 등록
		return new BCryptPasswordEncoder();
	}
}
>>>>>>> main
*/
=======
package com.chakak.config;

import com.chakak.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

				// 로그인, 회원가입 페이지는 JWT 검증 스킵
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
									.getBean(UserDetailsService.class);
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

				// Authorization 헤더에서 토큰 추출
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
						.permitAll().requestMatchers("/mypage", "/edit", "/withdraw")
						.authenticated().anyRequest().authenticated())
				.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.formLogin(AbstractHttpConfigurer::disable).httpBasic(AbstractHttpConfigurer::disable)
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true)
						.deleteCookies("token"));

		return http.build();
	}
}
>>>>>>> main
