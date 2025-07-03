package com.chakak.config;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication; 
import org.springframework.security.core.GrantedAuthority; 
import org.springframework.security.core.userdetails.UserDetails; 

import java.security.Key; 
import java.util.Date;
import java.util.stream.Collectors; 

@Component
public class JwtTokenProvider {

    private final String SECRET_KEY = "my-very-very-secure-and-long-secret-key-which-is-over-32bytes"; // 32바이트 이상

 
  
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
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes()) // 0.9.1 버전의 signWith
                .compact();
    }


    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            
            Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(token);
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
        return Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(token).getBody().getSubject();
    }
}