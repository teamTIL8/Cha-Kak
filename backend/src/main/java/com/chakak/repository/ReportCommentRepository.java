package com.chakak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.chakak.domain.Comment;
import com.chakak.domain.Report;

public interface ReportCommentRepository extends JpaRepository<Comment, Long>{

	@Query(value = "SELECT * FROM COMMENT WHERE REPORT_ID = :reportId", nativeQuery = true)
	List<Comment> findByReportId(Long reportId);

}
