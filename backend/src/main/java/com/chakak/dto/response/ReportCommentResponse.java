package com.chakak.dto.response;

import java.time.LocalDateTime;

import com.chakak.domain.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ReportCommentResponse {
	
	private Long commentId;
	private String userId;
	private String content;
	private LocalDateTime createdAt;
	
	public ReportCommentResponse (Comment comment) {
		this.commentId = comment.getCommentId();
		this.userId = comment.getUserId();
		this.content = comment.getContent();
		this.createdAt = comment.getCreatedAt();
	}
}
