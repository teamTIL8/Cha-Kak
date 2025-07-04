package com.chakak.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReportCommentRequest {
	
	private Long commentId;
	private Long reportId;
	private String userId;
	
	@NotBlank(message = "댓글 내용은 필수입니다.")
	private String content; 
}
