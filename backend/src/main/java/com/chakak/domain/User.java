package com.chakak.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Builder // 필요 시 생성자 대신 빌더 사용
public class User {
	
	@Id
	@Column(name = "user_id", length = 100)
	private String userId;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false, unique = true, length = 255)
	private String email;
	
	@Column(nullable = false)
	private String name;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	
	@Column(nullable = false, name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();
	
	@Column(nullable = false, name = "updated_at")
	private LocalDateTime updateAt;
	
	@Column(name = "is_deleted")
	private boolean isDeleted = false;
	
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;
	
	public enum Role{
		USER, ADMIN
	}
}