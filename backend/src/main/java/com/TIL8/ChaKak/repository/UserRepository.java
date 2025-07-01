package com.TIL8.ChaKak.repository;

import com.TIL8.ChaKak.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	Optional<User> findByUserId(String userId);

	boolean existsByUserId(String userId);

	@Query(value = "SELECT * FROM User WHERE user_id = ?1 AND is_deleted = true", nativeQuery = true)
	Optional<User> findDeletedByUserId(String userId);

	@Query(value = "SELECT * FROM User WHERE email = ?1 AND is_deleted = true", nativeQuery = true)
	Optional<User> findDeletedByEmail(String email);
}