package com.chakak.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Report {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reportId;
	private String title;
	private String userId;
	private String vehicleNumber;

	@Column(insertable = false, updatable = false)
	private LocalDateTime reportTime;
	
	private String violationType;
	private String address; //지도상 주소
	private double latitude; // 위도
	private double longitude; // 경도
	private String description;
	
	@Column(insertable = false, updatable = false)
	private LocalDateTime createdAt;
}
