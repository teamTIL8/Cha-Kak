package com.chakak.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.chakak.common.enums.Violation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long commentId;
	
	private String userId;
	private String content;
	private LocalDateTime createdAt;
	
	@ManyToOne
	@JoinColumn(name = "REPORT_ID")
	private Report report;
}
