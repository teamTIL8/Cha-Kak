package com.chakak.repository;

import com.chakak.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmailAndIsDeletedFalse(String email);

    Optional<User> findByUserIdAndIsDeletedFalse(String userId);

    boolean existsByEmailAndIsDeletedFalse(String email);

    boolean existsByUserIdAndIsDeletedFalse(String userId);

    Optional<User> findByEmailAndIsDeletedTrue(String email);

    Optional<User> findByUserIdAndIsDeletedTrue(String userId);

    boolean existsByEmailAndIsDeletedTrue(String email);

    boolean existsByUserIdAndIsDeletedTrue(String userId);


    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUserId(String userId);

    @Query("SELECT u FROM User u WHERE u.isDeleted = false AND u.role = 'USER'")
    List<User> findAllActiveUsersOnly();

    @Query("SELECT u FROM User u WHERE u.role = 'ADMIN'")
    List<User> findAdmins();

    @Query(value = "SELECT * FROM User WHERE user_id = ?1 AND is_deleted = true", nativeQuery = true)
    Optional<User> findDeletedByUserId(String userId);

    @Query(value = "SELECT * FROM User WHERE email = ?1 AND is_deleted = true", nativeQuery = true)
    Optional<User> findDeletedByEmail(String email);
}
