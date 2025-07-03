package com.chakak.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.chakak.domain.User;

public interface UserRepository extends JpaRepository<User, String>{
	
    // userId 기준으로 사용자 조회
    Optional<User> findByUserId(String userId);
	
    // 이메일로 사용자 조회 (로그인 시 사용 가능)
	Optional<User> findByEmail(String email);
	
    // 삭제되지 않은 일반 사용자만 조회 (관리자 제외)
    @Query("SELECT u FROM User u WHERE u.isDeleted = false AND u.role = 'USER'")
    List<User> findAllActiveUsersOnly();
    
    // 관리자만 조회
    @Query("SELECT u FROM User u WHERE u.role = 'ADMIN'")
    List<User> findAdmins();
}