package com.chakak.dto;

import java.time.LocalDateTime;

import com.chakak.domain.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
	
	private Long commentId;
	private String userId;
	private Long reportId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updateAt;
	
	public static CommentDto fromEntity(Comment comment) {
		return CommentDto.builder()
				.commentId(comment.getCommentId())
				.userId(comment.getUser().getUserId())
				.reportId(comment.getReport().getReportId())
				.content(comment.getContent())
				.createdAt(comment.getCreatedAt())
				.updateAt(comment.getUpdatedAt())
				.build();
	}

}
