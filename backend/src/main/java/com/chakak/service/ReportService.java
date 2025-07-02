package com.chakak.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.chakak.common.enums.Violation;
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
                                         String reportDateStr, String violationTypeStr, String startDateStr, String endDateStr,
                                         String keyword, Pageable pageable) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate reportDate = null;
        LocalDate startDate = null;
        LocalDate endDate = null;
        Violation violationType = null;

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
            if (violationTypeStr != null && !violationTypeStr.isEmpty()) {
                violationType = Violation.valueOf(violationTypeStr.toUpperCase());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("날짜 형식 또는 위반유형이 잘못되었습니다.");
        }

        Specification<Report> spec = Specification.where(null);

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
    public ReportDto getReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new EntityNotFoundException("Report not found"));

        report.setViewCnt(report.getViewCnt() + 1);
        reportRepository.save(report);

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

    public ReportDto getReportDetail(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 신고 없음"));
        report.setViewCnt(report.getViewCnt() + 1);
        return ReportDto.fromEntity(report);
    }
} 
