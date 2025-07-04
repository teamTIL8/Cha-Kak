package com.chakak.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user") // ✔ main 브랜치의 설정 유지
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE user SET is_deleted = true, deleted_at = NOW() WHERE user_id = ?")
@Where(clause = "is_deleted = false")
public class User implements UserDetails {

    @Id
    @Column(name = "user_id", length = 100)
    private String userId;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "ENUM('USER', 'ADMIN')")
    @ColumnDefault("'USER'")
    private Role role;

    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("false")
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // UserDetails 인터페이스 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isDeleted;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isDeleted;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isDeleted;
    }

    @Override
    public boolean isEnabled() {
        return !isDeleted;
    }

    // 비즈니스 로직 메서드
    public void updateProfile(String name, String password) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
        if (password != null && !password.trim().isEmpty()) {
            this.password = password;
        }
    }

    public void deactivateUser() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void reactivateUser() {
        this.isDeleted = false;
        this.deletedAt = null;
    }

    public enum Role {
        USER, ADMIN
    }
}
