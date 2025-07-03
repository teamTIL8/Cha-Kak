package com.chakak.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReportResponse {
	
	private Long reportId;
	private String title;
	private String userId;
	private String vehicleNumber;
	private LocalDateTime reportTime;
	private String violationType;
	private Long viewCount = 0L;
	
	private String address; //지도상 주소
	private double latitude; // 위도
	private double longitude; // 경도
	
	private String maskedCarNumber; 
	
	private String description;
}
