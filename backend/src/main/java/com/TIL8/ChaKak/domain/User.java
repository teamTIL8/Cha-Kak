package com.TIL8.ChaKak.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = User.TABLE_NAME) // 상수 사용
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE User SET is_deleted = true, deleted_at = NOW() WHERE user_id = ?")
@Where(clause = "is_deleted = false")
public class User {

	// ===== 상수 정의 시작 =====
	public static final String TABLE_NAME = "User"; // 데이터베이스 테이블 이름
	public static final String COLUMN_USER_ID = "user_id";
	public static final String COLUMN_PASSWORD = "password";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_ROLE = "role";
	public static final String COLUMN_CREATED_AT = "created_at";
	public static final String COLUMN_UPDATED_AT = "updated_at";
	public static final String COLUMN_IS_DELETED = "is_deleted";
	public static final String COLUMN_DELETED_AT = "deleted_at";

	public static final int LENGTH_USER_ID = 100;
	public static final int LENGTH_COMMON_STRING = 255; // password, email, name

	public static final String ROLE_ENUM_DEFINITION = "ENUM('USER', 'ADMIN')";
	public static final String DEFAULT_ROLE = "'USER'"; // DB @ColumnDefault 문자열

	public static final String DEFAULT_IS_DELETED_VALUE = "false"; // DB @ColumnDefault 문자열
	public static final Boolean DEFAULT_IS_DELETED_BOOLEAN = false; // Java 코드에서 사용할 boolean 값
	// ===== 상수 정의 끝 =====

	@Id
	@Column(name = COLUMN_USER_ID, length = LENGTH_USER_ID) // 상수 사용
	private String userId; // 회원 고유 ID (PK)

	@Column(nullable = false, length = LENGTH_COMMON_STRING) // 상수 사용
	private String password; // 비밀번호 (암호화 저장)

	@Column(nullable = false, unique = true, length = LENGTH_COMMON_STRING) // 상수 사용
	private String email; // 이메일 주소

	@Column(nullable = false, length = LENGTH_COMMON_STRING) // 상수 사용
	private String name; // 사용자 이름

	@Enumerated(EnumType.STRING) // Enum 타입 매핑
	@Column(nullable = false, columnDefinition = ROLE_ENUM_DEFINITION) // 상수 사용
	@ColumnDefault(DEFAULT_ROLE) // 상수 사용
	private Role role; // 사용자 권한 (USER/ADMIN)

	@CreationTimestamp // 엔티티 생성 시 자동으로 시간 기록
	@Column(name = COLUMN_CREATED_AT, nullable = false, updatable = false) // 상수 사용
	private LocalDateTime createdAt; // 생성일시

	@UpdateTimestamp // 엔티티 업데이트 시 자동으로 시간 기록
	@Column(name = COLUMN_UPDATED_AT) // 상수 사용
	private LocalDateTime updatedAt; // 수정일시

	@Column(name = COLUMN_IS_DELETED, nullable = false) // 상수 사용
	@ColumnDefault(DEFAULT_IS_DELETED_VALUE) // 상수 사용
	private Boolean isDeleted; // 탈퇴 여부

	@Column(name = COLUMN_DELETED_AT) // 상수 사용
	private LocalDateTime deletedAt; // 탈퇴 일시

	// Builder 패턴을 위한 생성자
	public User(String userId, String password, String email, String name, Role role) {
		this.userId = userId;
		this.password = password;
		this.email = email;
		this.name = name;
		this.role = role;
		this.isDeleted = DEFAULT_IS_DELETED_BOOLEAN; // 상수 사용
	}

	// Role Enum 정의
	public enum Role {
		USER, ADMIN
	}

	// ===== 비즈니스 로직 =====

	/**
	 * 사용자 정보(이름, 비밀번호)를 수정합니다.
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
	 * 사용자를 비활성화(소프트 삭제)합니다.
	 */
	public void deactivateUser() {
		this.isDeleted = true;
		this.deletedAt = LocalDateTime.now();
	}

	/**
	 * 사용자를 재활성화합니다. (소프트 삭제 취소)
	 */
	public void reactivateUser() {
		this.isDeleted = false;
		this.deletedAt = null;
	}

	/**
	 * 사용자 ID를 설정합니다.
	 * @param userId 새로운 사용자 ID
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 이메일 주소를 설정합니다.
	 * @param email 새로운 이메일 주소
	 */
	public void setEmail(String email) {
		this.email = email;
	}
}