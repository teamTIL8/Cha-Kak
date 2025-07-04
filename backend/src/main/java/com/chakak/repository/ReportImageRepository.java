package com.chakak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chakak.domain.Report;
import com.chakak.domain.ReportImage;

public interface ReportImageRepository extends JpaRepository<ReportImage, Long>{

	// Report 와 관계된 Report Image 조회
	List<ReportImage> findByReport(Report report);
}
