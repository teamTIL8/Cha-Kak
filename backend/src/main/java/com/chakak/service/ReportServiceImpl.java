package com.chakak.service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.chakak.common.enums.Violation;
import com.chakak.domain.Report;
import com.chakak.domain.User;
import com.chakak.dto.ReportDto;
import com.chakak.repository.ReportRepository;
import com.chakak.repository.ReportSpecification;
import com.chakak.repository.UserRepository;
import com.chakak.util.AddressUtils;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository repository;
    private final UserRepository userRepository;

    @Override
    public Report save(Report report) {
        String userId = report.getUserId();
        User user = userRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자"));

        report.setUser(user);
        String address = report.getAddress();
        String locationType = AddressUtils.extractRegion(address);
        report.setLocationType(locationType);

        return repository.save(report);
    }

    @Override
    public List<Report> findAll() {
        return repository.findAll();
    }

    @Override
    public Report findById(Long reportId) {
        return repository.findById(reportId).orElse(null);
    }

    // ✅ 전체 조회 및 필터링 조회
    @Override
    public Page<ReportDto> getAllReports(String carNumber, String location,
                                         String reportDateStr, String violationTypeStr,
                                         String startDateStr, String endDateStr,
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

        if (carNumber != null && !carNumber.isEmpty()) {
            spec = spec.and(ReportSpecification.carNumberContains(carNumber));
        }

        if (location != null && !location.isEmpty()) {
            spec = spec.and(ReportSpecification.locationContains(location));
        }

        if (violationType != null) {
            spec = spec.and(ReportSpecification.violationTypeEquals(violationType));
        }

        if (reportDate != null) {
            spec = spec.and(ReportSpecification.reportDateEquals(reportDate));
        } else if (startDate != null && endDate != null) {
            spec = spec.and(ReportSpecification.reportDateBetween(startDate, endDate));
        }

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and(ReportSpecification.keywordContains(keyword));
        }

        Page<Report> reportPage = repository.findAll(spec, pageable);

        return reportPage.map(ReportDto::fromEntity);
    }

    @Override
    public List<ReportDto> getMyReports(String userId) {
        return repository.findByUser_UserId(userId)
                .stream()
                .map(ReportDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public ReportDto getReport(Long reportId) {
        Report report = repository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report not found"));

        report.setViewCount(report.getViewCount() + 1);
        repository.save(report);

        return convertToDto(report);
    }

    private ReportDto convertToDto(Report report) {
        return ReportDto.builder()
                .reportId(report.getReportId())
                .title(report.getTitle())
                .vehicleNumber(report.getVehicleNumber())
                .address(report.getAddress())
                .viewCnt(report.getViewCount())
                .build();
    }

    @Override
    public ReportDto getReportDetail(Long id) {
        Report report = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 신고 없음"));
        report.setViewCount(report.getViewCount() + 1);
        return ReportDto.fromEntity(report);
    }

    @Override
    public void deleteReport(Long reportId) {
        Report report = repository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 제보입니다."));

        report.getReportImages().forEach(image -> {
            File file = new File(image.getImgPath());
            if (file.exists()) {
                file.delete();
            }
        });

        repository.delete(report);
    }
}
