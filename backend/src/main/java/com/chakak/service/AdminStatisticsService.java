package com.chakak.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.chakak.common.enums.Violation;
import com.chakak.domain.Report;
import com.chakak.dto.response.FrequentAddressDto;
import com.chakak.dto.response.ReportCoordinateDto;
import com.chakak.dto.response.TopVehicleReportDto;
import com.chakak.dto.response.ViolationTypeStatDto;
import com.chakak.repository.ReportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminStatisticsService {

    private final ReportRepository reportRepository;

    /**
     * 중복 제보 차량 상위 10개 반환
     */
    public List<TopVehicleReportDto> getTopVehicleReports() {
        return reportRepository.findTop10VehicleReports();
    }
    
    
    /**
     * 제보 유형별 분포 통계
     */
    public List<ViolationTypeStatDto> getViolationTypeStats() {
        return reportRepository.getViolationTypeStats();
    }
    
    /**
     * 반복 제보 위치 통계 (지역별)
     */
    public List<FrequentAddressDto> getFrequentAddresses() {
        return reportRepository.getFrequentAddresses();
    }
    
    /**
     * 지역별 제보 히트맵 표시용 좌표 데이터
     */
    public List<ReportCoordinateDto> getAllReportCoordinates() {
        return reportRepository.getAllReportCoordinates();
    }
    
    public List<Report> getReportsByViolationType(Violation violationType) {
        return reportRepository.findByViolationType(violationType);
    }
}