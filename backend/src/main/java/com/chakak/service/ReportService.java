package com.chakak.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.chakak.domain.Report;
import com.chakak.dto.ReportDto;
import com.chakak.repository.ReportRepository;
import com.chakak.repository.ReportSpecification;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;


@Service

public class ReportService {
	

    @Autowired
    private ReportRepository reportRepository;

    public Page<ReportDto> getAllReports(String carNumber, String location,
                                         String reportDateStr,String violationType, String startDateStr, String endDateStr,
                                         String keyword, Pageable pageable) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate reportDate = null;
        LocalDate startDate = null;
        LocalDate endDate = null;

        try {
            if (reportDateStr != null && !reportDateStr.isEmpty()) {
                reportDate = LocalDate.parse(reportDateStr, formatter);
            }
            if (startDateStr != null && !startDateStr.isEmpty()) {
                startDate = LocalDate.parse(startDateStr, formatter);
            }
            if (endDateStr != null && !endDateStr.isEmpty()) {
                endDate = LocalDate.parse(endDateStr, formatter);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("날짜 형식은 yyyy-MM-dd 이어야 합니다.");
        }

        // 전체 목록 조회 동작
        Specification<Report> spec = Specification.where(null); // 필터링 파라미터를 null로 하여 spec 조건을 추가하지 않음

        spec = spec.and(ReportSpecification.carNumberContains(carNumber))
                   .and(ReportSpecification.locationContains(location))
                   .and(ReportSpecification.violationTypeEquals(violationType));

        if (reportDate != null) {
            spec = spec.and(ReportSpecification.reportDateEquals(reportDate));
        } else if (startDate != null && endDate != null) {
            spec = spec.and(ReportSpecification.reportDateBetween(startDate, endDate));
        }

        spec = spec.and(ReportSpecification.keywordContains(keyword));

        Page<Report> reportPage = reportRepository.findAll(spec, pageable);

        return reportPage.map(ReportDto::fromEntity);
    }
	
	public List<ReportDto> getMyReports(String userId) {
		return reportRepository.findByUser_UserId(userId)
				.stream()
				.map(ReportDto::fromEntity)
				.toList();
	}
	@Transactional
	public  ReportDto getReport(Long reportId) {
	    Report report = reportRepository.findById(reportId)
	        .orElseThrow(() -> new EntityNotFoundException("Report not found"));

	    // 조회수 1 증가
	    report.setViewCnt(report.getViewCnt() + 1);
	    reportRepository.save(report);  // 반드시 저장해줘야 함

	    return convertToDto(report);
	}
	
	private ReportDto convertToDto(Report report) {
	    return ReportDto.builder()
	            .reportId(report.getReportId())
	            .title(report.getTitle())
	            .vehicleNumber(report.getVehicleNumber())  
	            .address(report.getAddress())
	            .viewCnt(report.getViewCnt())
	            .build();
	}

	public ReportDto getReportDetail(Long id ) {
		Report report = reportRepository.findById(id)
				.orElseThrow(()->new EntityNotFoundException("해당 신고 없음"));
		report.setViewCnt(report.getViewCnt()+1);
		return ReportDto.fromEntity(report);
	}

}
