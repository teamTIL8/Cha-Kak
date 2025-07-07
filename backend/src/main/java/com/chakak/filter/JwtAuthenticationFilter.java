package com.chakak.filter;

import com.chakak.service.AuthService;
import com.chakak.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final AuthService authService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {
	    String token = getTokenFromRequest(request);
	    log.debug("[JWT Filter] Token extracted: {}", token != null ? "Yes" : "No");

	    if (token != null) {
	        try {
	            String username = jwtUtil.extractUsername(token);
	            log.debug("[JWT Filter] Username from token: {}", username);

	            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	                UserDetails userDetails = authService.loadUserByUsername(username);
	                log.debug("[JWT Filter] UserDetails loaded: {}", userDetails != null ? "Yes" : "No");

	                if (jwtUtil.validateToken(token, userDetails)) {
	                    UsernamePasswordAuthenticationToken authToken =
	                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                    SecurityContextHolder.getContext().setAuthentication(authToken);
	                    log.debug("[JWT Filter] Authentication successful for user: {}", username);
	                } else {
	                    log.warn("[JWT Filter] Token validation failed");
	                }
	            }
	        } catch (Exception e) {
	            log.error("[JWT Filter] Error during authentication", e);
	        }
	    } else {
	        log.debug("[JWT Filter] No token found in request");
	    }
	    filterChain.doFilter(request, response);
	}

	private String getTokenFromRequest(HttpServletRequest request) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("token".equals(cookie.getName())) {
				    log.debug("Found JWT token in cookie."); //
					return cookie.getValue();
				}
			}
		}

		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
		    log.debug("Found JWT token in Authorization header."); //
			return bearerToken.substring(7);
		}

		return null;
	}
		////////////
		/*final String token = getTokenFromRequest(request);

	  
		if (token != null && jwtUtil.validateToken(token)) {
			String username = jwtUtil.extractUsername(token);

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				try {
					UserDetails userDetails = authService.loadUserByUsername(username);

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

		filterChain.doFilter(request, response);
	}

	private String getTokenFromRequest(HttpServletRequest request) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("token".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}

		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}

		return null;
	}*/
	
}
