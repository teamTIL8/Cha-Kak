package com.chakak.dto;



import com.chakak.domain.Report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
// 신고 목록 조회 (그냥 메인화면에 있는 일반 리스트 화면) -> /reports, 마이페이지에서 사용할 거임
public class ReportDto {
	private Long reportId;
	private String title;
	private String vehicleNumber;
	private String address;
	private int viewCnt;
	
	
	
	
	
	public static ReportDto fromEntity(Report report) {
		return new ReportDto(
				report.getReportId(),
				report.getTitle(),
				report.getVehicleNumber(),
				report.getAddress(),
				report.getViewCnt()
				);
	}
	
	

}
