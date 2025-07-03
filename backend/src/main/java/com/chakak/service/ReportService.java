package com.chakak.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.chakak.domain.Report;
import com.chakak.dto.ReportDto;

public interface ReportService {
	
	public Report save(Report report);

	public List<Report> findAll();

	public Report findById(Long reportId);
	
	public Page<ReportDto> getAllReports(String carNumber, String location,
		      String reportDateStr, String violationTypeStr, String startDateStr, String endDateStr,
		      String keyword, Pageable pageable);
	
	
	public List<ReportDto> getMyReports(String userId);
	public ReportDto getReport(Long reportId);
	public ReportDto getReportDetail(Long id);
	
}

