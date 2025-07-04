package com.chakak.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication; 
import org.springframework.security.core.GrantedAuthority; 
import org.springframework.security.core.userdetails.UserDetails; 

import java.security.Key; 
import java.util.Date;
import java.util.stream.Collectors; 

@Component
public class JwtTokenProvider {

    // SECRET_KEY 32바이트 이상이어야 하고, 바이트 배열로 키 생성
    private final String SECRET_KEY_STRING = "my-very-very-secure-and-long-secret-key-which-is-over-32bytes";

    private final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());

    public String createToken(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();

        long validityInMilliseconds = 1000L * 60 * 60; // 1시간 
        Date validity = new Date(now + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(userDetails.getUsername()) 
                .claim("auth", authorities)
                .setIssuedAt(new Date(now)) 
                .setExpiration(validity) 
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)  // 최신 버전 방식
                .compact();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            System.out.println("잘못된 JWT 서명입니다.");
        } catch (MalformedJwtException e) {
            System.out.println("잘못된 형식의 JWT 토큰입니다."); 
        } catch (ExpiredJwtException e) {
            System.out.println("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            System.out.println("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT 토큰이 잘못되었습니다."); 
        }
        return false;
    }

    // 토큰에서 사용자 이름(subject) 추출
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
