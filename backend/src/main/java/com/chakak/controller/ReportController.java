package com.chakak.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.chakak.domain.Report;
import com.chakak.dto.request.ReportRequest;
import com.chakak.service.ReportImageService;
import com.chakak.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {
	
	private final ReportService service;
	private final ReportImageService reportImageService;
	
	/**
	 * 제안 신고 내역 저장
	 * */
	@PostMapping
	public ResponseEntity<?> saveReport(@RequestBody ReportRequest reportDto){
		Report report = new Report();
		report.setTitle(reportDto.getTitle());
		report.setUserId(reportDto.getUserId());
		report.setViolationType(reportDto.getViolationType());
		report.setVehicleNumber(reportDto.getVehicleNumber());
		report.setDescription(reportDto.getDescription());
		report.setAddress(reportDto.getAddress());
		report.setLatitude(reportDto.getLatitude());
		report.setLongitude(reportDto.getLongitude());
		
		Report savedReport = service.save(report);
		return ResponseEntity.ok(savedReport.getReportId());
	}
	
	/**
	 * 제안 신고 내역(첨부 이미지) 저장 
	 * */
	@PostMapping("/upload/{reportId}")
    public ResponseEntity<?> uploadFiles(@PathVariable Long reportId,
                                         @RequestParam("files") List<MultipartFile> files) {

		reportImageService.save(reportId, files);
		
        return ResponseEntity.ok("Files Uploaded");
    }
	
	
}
