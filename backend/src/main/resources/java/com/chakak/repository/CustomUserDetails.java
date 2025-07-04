package com.chakak.repository; // 패키지 변경

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.chakak.domain.User; // User 엔티티 경로에 맞게 수정

import lombok.Getter; // lombok 사용 시

@Getter
public class CustomUserDetails implements UserDetails { // 클래스 이름 변경

    private final String userId;
    private final String password;
    private final User user; // 필요한 경우 User 객체 포함

    public CustomUserDetails(User user) {
        this.userId = user.getUserId();
        this.password = user.getPassword();
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // User 엔티티에 Role 정보가 있다면 아래와 같이 사용
        if (user != null && user.getRole() != null) {
            return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER")); // 기본 역할 설정
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}