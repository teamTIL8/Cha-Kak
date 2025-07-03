package com.chakak.service;

import java.util.List;

import com.chakak.domain.Report;

public interface ReportService {
	
	public Report save(Report report);

	public List<Report> findAll();

	public Report findById(Long reportId);
	
}
