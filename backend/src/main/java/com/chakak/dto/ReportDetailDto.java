package com.chakak.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.chakak.domain.Report;



@Getter
@NoArgsConstructor
@AllArgsConstructor
// 신고 상세 조회 (상세 보기 화면을 위해 DetailDto를 만듦 ) -> /reports/{id} 에 사용할 예정
public class ReportDetailDto {
    private Long reportId;
    private String userId;
    private String title;
    private String vehicleNumber;
    private String violationType;
    private String address;
    private Double latitude;
    private Double longitude;
    private String description;
    private int viewCnt;
    private LocalDateTime reportTime;
    private LocalDateTime createdAt;

    public static ReportDetailDto fromEntity(Report report) {
        return new ReportDetailDto(
                report.getReportId(),
                report.getUser().getUserId(),
                report.getTitle(),
                report.getVehicleNumber(),
                report.getViolationType(),
                report.getAddress(),
                report.getLatitude(),
                report.getLongitude(),
                report.getDescription(),
                report.getViewCnt(),
                report.getReportTime(),
                report.getCreatedAt()
        );
    }
}