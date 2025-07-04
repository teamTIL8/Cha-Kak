package com.chakak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chakak.domain.Comment;



@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
	// 특정 사용자가 자겅한 댓글 목록 조회
	List<Comment> findByUser_UserId(String userId);

}
