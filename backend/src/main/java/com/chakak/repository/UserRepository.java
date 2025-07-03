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
}