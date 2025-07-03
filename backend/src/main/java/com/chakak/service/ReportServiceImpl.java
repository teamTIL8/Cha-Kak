package com.chakak.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chakak.domain.Report;
import com.chakak.repository.ReportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{
	
	private final ReportRepository repository;
	
	@Override
	public Report save(Report report) {
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

}
