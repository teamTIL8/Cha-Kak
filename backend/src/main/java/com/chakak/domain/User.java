package com.chakak.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data; // @Getter, @Setter, @EqualsAndHashCode, @ToString 포함
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault; // ColumnDefault를 위해 필요 (DB 스키마에 따라 BIT(1) DEFAULT 0 와 매핑)
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete; // 소프트 삭제를 위한 SQL
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where; // 소프트 삭제된 엔티티 조회 필터
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user") // 테이블 이름은 직접 명시하거나, 필요시 상수로 관리
@Data // @Getter, @Setter 등을 포함하므로 @Getter, @Setter는 제거
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE users SET is_deleted = true, deleted_at = NOW() WHERE user_id = ?") // 테이블 이름 수정
@Where(clause = "is_deleted = false") // is_deleted가 false인 레코드만 조회
public class User implements UserDetails { // UserDetails 인터페이스 구현 유지

    @Id
    @Column(name = "user_id", length = 100)
    private String userId;

    @Column(name = "email", nullable = false, unique = true, length = 255) // 길이 직접 명시 (상수 제거)
    private String email;

    @Column(name = "name", nullable = false, length = 255) // 길이 직접 명시 (상수 제거)
    private String name;

    @Column(name = "password", nullable = false, length = 255) // 길이 직접 명시 (상수 제거)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "ENUM('USER', 'ADMIN')") // DB 컬럼 정의 직접 명시 (상수 제거)
    @ColumnDefault("'USER'") // DB 기본값 직접 명시 (상수 제거)
    private Role role;

    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("false") // DB 기본값 직접 명시 (상수 제거)
    private Boolean isDeleted = false; // Java 단에서도 기본값 설정. @ColumnDefault와 함께 사용시 JPA/Hibernate가 처리

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // UserDetails 인터페이스 구현 (HEAD 브랜치 유지)
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

    // origin/main 브랜치에서 추가된 비즈니스 로직 메서드 통합
    /**
     * 사용자 정보(이름, 비밀번호)를 수정
     * @param name 새로운 이름
     * @param password 새로운 비밀번호 (암호화된 상태)
     */
    public void updateProfile(String name, String password) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
        if (password != null && !password.trim().isEmpty()) {
            this.password = password;
        }
    }

    /**
     * 사용자를 비활성화(소프트 삭제)
     */
    public void deactivateUser() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * 사용자를 재활성화 (소프트 삭제 취소)
     */
    public void reactivateUser() {
        this.isDeleted = false;
        this.deletedAt = null;
    }

    // @Builder와 @AllArgsConstructor가 모든 필드를 포함한 생성자를 제공하므로
    // origin/main에 명시적으로 정의되었던 Builder 패턴용 생성자는 제거합니다.
    // 또한 @Data 어노테이션이 getter/setter를 자동 생성하므로 setUserId, setEmail 메서드도 제거합니다.

    public enum Role {
        USER, ADMIN
    }
}