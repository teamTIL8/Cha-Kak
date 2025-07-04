package com.chakak.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "NOTICE")
public class Notice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long noticeId;

	private String title;
	private String content;
	private Integer view_count = 0;

	@ManyToOne
	@JoinColumn(name = "created_id", nullable = false, updatable = false)
	private User createdUser;
	private LocalDateTime created_at;

	@ManyToOne
	@JoinColumn(name = "updated_id")
	private User updatedUser;
	private LocalDateTime updated_at;

}
