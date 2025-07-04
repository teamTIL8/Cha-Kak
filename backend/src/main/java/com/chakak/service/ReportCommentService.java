package com.chakak.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.chakak.domain.Comment;
import com.chakak.domain.Report;
import com.chakak.dto.response.ReportCommentResponse;
import com.chakak.repository.ReportCommentRepository;
import com.chakak.repository.ReportRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportCommentService {
	
	private final ReportCommentRepository repository;
	private final ReportRepository reportRepository;
	
	// 댓글 저장
	public Comment save(Comment comment, Long reportId) {
		
		Report report = reportRepository.findById(reportId).orElseThrow();;
		comment.setReport(report);
		comment.setCreatedAt(LocalDateTime.now());
		
		Comment saveComment =  repository.save(comment);
		
		return saveComment;
	}

	// 댓글 목록 조회
	public List<ReportCommentResponse> findByReportId(Long reportId) {
		List<Comment> comments = repository.findByReportId(reportId);
		
		return comments.stream().map(ReportCommentResponse::new).collect(Collectors.toList());
	}
	
	// 댓글 수정
	public Comment update(Comment comment, Long reportId) {
		Report report = reportRepository.findById(reportId).orElseThrow();
		comment.setReport(report);
		comment.setCreatedAt(LocalDateTime.now());
		
		Comment saveComment =  repository.save(comment);
		
		return saveComment;
	}
	
	// 댓글 삭제
	public void delete(Long commentId) {
		if (!repository.existsById(commentId)) {
            throw new IllegalArgumentException("댓글이 존재하지 않습니다. id: " + commentId);
        }
		
        repository.deleteById(commentId);
	}


}
