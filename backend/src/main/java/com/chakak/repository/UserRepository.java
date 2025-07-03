package com.chakak.repository;

import com.chakak.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByEmailAndIsDeletedFalse(String email);

	Optional<User> findByUserIdAndIsDeletedFalse(String userId);

	boolean existsByEmailAndIsDeletedFalse(String email);

	boolean existsByUserIdAndIsDeletedFalse(String userId);

	/**
	 * 탈퇴 회원 조회
	 */

	Optional<User> findByEmailAndIsDeletedTrue(String email);

	Optional<User> findByUserIdAndIsDeletedTrue(String userId);

	boolean existsByEmailAndIsDeletedTrue(String email);

	boolean existsByUserIdAndIsDeletedTrue(String userId);

		boolean existsByEmail(String email);

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
	boolean existsByUserId(String userId);

	@Query(value = "SELECT * FROM User WHERE user_id = ?1 AND is_deleted = true", nativeQuery = true)
	Optional<User> findDeletedByUserId(String userId);

	@Query(value = "SELECT * FROM User WHERE email = ?1 AND is_deleted = true", nativeQuery = true)
	Optional<User> findDeletedByEmail(String email);
