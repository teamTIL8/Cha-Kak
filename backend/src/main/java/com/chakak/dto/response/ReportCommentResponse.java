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
	private Long reportId;
	
	public ReportCommentResponse (Comment comment) {
		this.commentId = comment.getCommentId();
		this.userId = comment.getUser() != null ? comment.getUser().getUserId() : null;
		this.content = comment.getContent();
		this.createdAt = comment.getCreatedAt();
		this.reportId = comment.getReport() != null ? comment.getReport().getReportId() : null;
	}
}
