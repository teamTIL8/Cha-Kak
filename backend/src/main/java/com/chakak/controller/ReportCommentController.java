package com.chakak.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chakak.domain.Comment;
import com.chakak.domain.User;
import com.chakak.dto.CommentDto;
import com.chakak.dto.request.ReportCommentRequest;
import com.chakak.dto.response.ReportCommentResponse;
import com.chakak.service.CustomUserDetails;
import com.chakak.service.ReportCommentService;
import com.chakak.service.CommentService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class ReportCommentController {
	
	private final ReportCommentService service;
	private final CommentService commentService;
	
	/**
	 * CommentController에 있는 내 댓글 조회 메소드 여기로 옮김
	 * ✅ 내 댓글 조회
	 */
	@GetMapping("/users/me")
	public ResponseEntity<List<CommentDto>> getMyComments(@AuthenticationPrincipal CustomUserDetails userDetails) {
		 if (userDetails == null) {
	            return ResponseEntity.status(401).build();
	        }

	        String userId = userDetails.getUsername();
		return ResponseEntity.ok(commentService.getMyComment(userId));
	}

	
	
	/**
	 * 제보 댓글 저장
	 * */
	@PostMapping
	public ResponseEntity<?> saveReportComment(@RequestBody ReportCommentRequest commentDto, @AuthenticationPrincipal CustomUserDetails userDetails){
		 if (userDetails == null) {
	            return ResponseEntity.status(401).body("인증이 필요합니다.");
	        }
		Comment comment = new Comment();
		 User user = new User();
	     user.setUserId(userDetails.getUsername());
	     comment.setUser(user);
	     comment.setContent(commentDto.getContent());
		
		Comment savedComment = service.save(comment, commentDto.getReportId());
		List<ReportCommentResponse> commentList = service.findByReportId(commentDto.getReportId());
		return ResponseEntity.ok(commentList);
	}

	/**
	 * 제보 댓글 수정
	 * */
	@PutMapping
	public ResponseEntity<?> updateReportComment(@RequestBody ReportCommentRequest commentDto,  @AuthenticationPrincipal CustomUserDetails userDetails){

        if (userDetails == null) {
            return ResponseEntity.status(401).body("인증이 필요합니다.");
        }

        Comment comment = new Comment();
        User user = new User();
        user.setUserId(userDetails.getUsername());
        comment.setUser(user);
        comment.setCommentId(commentDto.getCommentId());
        comment.setContent(commentDto.getContent());

		Comment updatedComment = service.update(comment, commentDto.getReportId());
		
		List<ReportCommentResponse> commentList = service.findByReportId(commentDto.getReportId());
		return ResponseEntity.ok(commentList);
	}
	
	/**
	 * 제보 댓글 삭제
	 * */
	@DeleteMapping
	public ResponseEntity<?> deleteReportComment(@RequestBody ReportCommentRequest commentDto){
		service.delete(commentDto.getCommentId());
		
		List<ReportCommentResponse> commentList = service.findByReportId(commentDto.getReportId());
		return ResponseEntity.ok(commentList);
	}
	
	

}
