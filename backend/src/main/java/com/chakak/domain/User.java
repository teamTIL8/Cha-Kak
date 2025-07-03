package com.chakak.domain;

import com.chakak.common.constants.Role;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE USER SET IS_DELETED = true, DELETED_AT = NOW() WHERE USER_ID = ?")
@Where(clause = "IS_DELETED = false")
public class User {

    public static final String TABLE_NAME = "User";
    public static final String COLUMN_USER_ID = "USER_ID";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_ROLE = "ROLE";
    public static final String COLUMN_CREATED_AT = "CREATED_AT";
    public static final String COLUMN_UPDATED_AT = "UPDATED_AT";
    public static final String COLUMN_IS_DELETED = "IS_DELETED";
    public static final String COLUMN_DELETED_AT = "DELETED_AT";

    public static final int LENGTH_USER_ID = 100;
    public static final int LENGTH_COMMON_STRING = 255;

    public static final String ROLE_ENUM_DEFINITION = "ENUM('USER', 'ADMIN')";
    public static final String DEFAULT_ROLE = "'USER'";
    public static final String DEFAULT_IS_DELETED_VALUE = "false";
    public static final Boolean DEFAULT_IS_DELETED_BOOLEAN = false;

    @Id
    @Column(name = COLUMN_USER_ID, length = LENGTH_USER_ID)
    private String userId;

    @Column(name = COLUMN_PASSWORD, nullable = false, length = LENGTH_COMMON_STRING)
    private String password;

    @Column(name = COLUMN_EMAIL, nullable = false, unique = true, length = LENGTH_COMMON_STRING)
    private String email;

    @Column(name = COLUMN_NAME, nullable = false, length = LENGTH_COMMON_STRING)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = COLUMN_ROLE, nullable = false, columnDefinition = ROLE_ENUM_DEFINITION)
    @ColumnDefault(DEFAULT_ROLE)
    private Role role;

    @CreationTimestamp
    @Column(name = COLUMN_CREATED_AT, nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = COLUMN_UPDATED_AT)
    private LocalDateTime updatedAt;

    @Column(name = COLUMN_IS_DELETED, nullable = false)
    @ColumnDefault(DEFAULT_IS_DELETED_VALUE)
    private Boolean isDeleted = false;

    @Column(name = COLUMN_DELETED_AT)
    private LocalDateTime deletedAt;

    public User(String userId, String password, String email, String name, Role role) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;
        this.isDeleted = DEFAULT_IS_DELETED_BOOLEAN;
    }

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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
