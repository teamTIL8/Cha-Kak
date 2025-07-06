package com.chakak.filter;

import com.chakak.service.UserService;
import com.chakak.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserService userService;

	/*@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestURI = request.getRequestURI();
		log.debug("JWT Filter processing: {}", requestURI);

		// 로그인/회원가입/홈페이지는 JWT 검증 스킵
		if (requestURI.equals("/") || requestURI.equals("/login") || requestURI.equals("/register")
				|| requestURI.equals("/check-userid") || requestURI.equals("/check-email")
				|| requestURI.startsWith("/css/") || requestURI.startsWith("/js/") || requestURI.startsWith("/images/")
				|| requestURI.equals("/favicon.ico")) {
			log.debug("Skipping JWT validation for: {}", requestURI);
			filterChain.doFilter(request, response);
			return;
		}*/
	
	/////테스트 용 
	///테스트용 //////
		///
		 @Override
		    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		            throws ServletException, IOException {

		        String requestURI = request.getRequestURI();
		        log.debug("JWT Filter processing: {}", requestURI);

		        // 로그인/회원가입/홈페이지 및 제보 신청 등은 JWT 검증 스킵
		        if (requestURI.equals("/") || requestURI.equals("/login") || requestURI.equals("/register")
		                || requestURI.startsWith("/css/") || requestURI.startsWith("/js/")
		                || requestURI.startsWith("/images/") || requestURI.equals("/favicon.ico")) {
		            filterChain.doFilter(request, response);
		            return;
		        }
		        ///

		        final String token = getTokenFromRequest(request);

				if (token != null && jwtUtil.validateToken(token)) {
					String username = jwtUtil.extractUsername(token);

					if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
						try {
							UserDetails userDetails = userService.loadUserByUsername(username);

							if (jwtUtil.validateToken(token, userDetails)) {
								UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
										userDetails, null, userDetails.getAuthorities());
								authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
								SecurityContextHolder.getContext().setAuthentication(authToken);
							}
						} catch (Exception e) {
							log.error("JWT authentication error: ", e);
						}
					}
				}
				
				////🌟🌟🌟🌟🌟 테스트
				// ✅ 추가: JWT가 없고 인증 안 된 경우 → test1234 강제 인증
			    if (SecurityContextHolder.getContext().getAuthentication() == null) {
			        try {
			            UserDetails testUser = userService.loadUserByUsername("test1234");
			            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
			                    testUser, null, testUser.getAuthorities());
			            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			            SecurityContextHolder.getContext().setAuthentication(authToken);
			            log.warn("✅ test1234 유저로 테스트 인증 처리됨 (JWT 없음)");
			        } catch (Exception e) {
			            log.error("❌ test1234 사용자 인증 실패", e);
			        }
			    }
				////////////////////////////

				filterChain.doFilter(request, response);
			}
	
	
	
	

	private String getTokenFromRequest(HttpServletRequest request) {
		// 쿠키에서 토큰 추출
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("token".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}

		// Authorization 헤더에서 토큰 추출
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}

		return null;
	}
}