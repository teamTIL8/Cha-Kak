package com.chakak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.chakak.domain.Notice;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

	@Modifying
	@Transactional
	@Query("UPDATE Notice n SET n.view_count = n.view_count + 1 WHERE n.noticeId = :noticeId")
	void incrementViewCount(Long noticeId);
	
	@Query("SELECT n FROM Notice n ORDER BY n.created_at DESC")
	List<Notice> findAllByOrderByCreatedAtDesc();

}
