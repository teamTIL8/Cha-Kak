package com.chakak.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.chakak.domain.Report;
import com.chakak.dto.ReportDto;

public interface ReportService {

    Report save(Report report);

    List<Report> findAll();

    Report findById(Long reportId);

    Page<ReportDto> getAllReports(String carNumber, String location,
                                   String reportDateStr, String violationTypeStr,
                                   String startDateStr, String endDateStr,
                                   String keyword, Pageable pageable);

    List<ReportDto> getMyReports(String userId);

    ReportDto getReport(Long reportId);

    ReportDto getReportDetail(Long id);

    void deleteReport(Long reportId);  // 팀원 코드 유지
}
