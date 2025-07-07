package com.chakak.dto;



import java.time.LocalDateTime;

import com.chakak.common.enums.Violation;
import com.chakak.domain.Report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
// 신고 목록 조회 (그냥 메인화면에 있는 일반 리스트 화면) -> /reports, 마이페이지에서 사용할 거임
public class ReportDto {
	private Long reportId;
	private String title;
	private String userId;
	private String vehicleNumber;
	private LocalDateTime reportTime;
	
	private Violation violationType;
	private String address; //지도상 주소
	private double latitude; // 위도
	private double longitude; // 경도
	private String description;
	private Long viewCnt;
	
	public ReportDto(Long reportId, String title, String vehicleNumber, String address, Long viewCnt, Violation violationType, LocalDateTime reportTime) {
		this.reportId = reportId;
		this.title = title;
		this.vehicleNumber = vehicleNumber;
		this.address = address;
		this.viewCnt = viewCnt;
		this.violationType = violationType;
		this.reportTime = reportTime;
	}
	
	
	
	public static ReportDto fromEntity(Report report) {
		return new ReportDto(
				report.getReportId(),
				report.getTitle(),
				report.getVehicleNumber(),
				report.getAddress(),
				report.getViewCount(),
				report.getViolationType(),
				report.getReportTime()
				);
	}
	
	

}
