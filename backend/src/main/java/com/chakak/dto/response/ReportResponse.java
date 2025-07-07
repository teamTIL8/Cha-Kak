package com.chakak.dto.response;

import java.time.LocalDateTime;

import com.chakak.common.enums.Violation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportResponse {
	
	private Long reportId;
	private String title;
	private String userId;
	private String vehicleNumber;
	private LocalDateTime reportTime;
	private Long viewCount;

	
	private Violation violationType;
	private String address; //지도상 주소
	private double latitude; // 위도
	private double longitude; // 경도
	private String description;
	
}
