package com.chakak.domain;

import java.time.LocalDateTime;

import com.chakak.common.enums.Violation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@NoArgsConstructor
@AllArgsConstructor
public class Report {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reportId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="USER_ID")
	private User user;
	private String title;
	private String vehicleNumber;
	private LocalDateTime reportTime;
	
	@Enumerated(EnumType.STRING)
	private Violation violationType;
	
	private String address;
	private Double latitude;
	private Double longitude;
	@Column(name = "DESCRIPTION")
	private String description;
	
	private int viewCnt = 0;
	private LocalDateTime createdAt = LocalDateTime.now();
	private LocalDateTime updatedAt;
	

}
