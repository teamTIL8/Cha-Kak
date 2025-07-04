package com.chakak.domain;

import java.time.LocalDateTime;
<<<<<<< HEAD

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
=======
import java.util.List;

import com.chakak.common.enums.Violation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
>>>>>>> main
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
	
<<<<<<< HEAD
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REPORT_ID")  // 대문자 맞춤
	private Report report;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")    // 대문자 맞춤
	private User user;
	
	private String content;
	private LocalDateTime createdAt = LocalDateTime.now();
	private LocalDateTime updatedAt = LocalDateTime.now();
=======
	private String userId;
	private String content;
	private LocalDateTime createdAt;
	
	@ManyToOne
	@JoinColumn(name = "REPORT_ID")
	private Report report;
>>>>>>> main
}
