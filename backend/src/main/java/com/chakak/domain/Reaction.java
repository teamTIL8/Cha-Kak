package com.chakak.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class Reaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reactionId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REPORT_ID")  // 대문자 맞춤
	private Report report;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")    // 대문자 맞춤
	private User user;
	
	private String reactionType; // 좋아요, 싫어요 타입
	private LocalDateTime createdAt = LocalDateTime.now();
}
