package com.chakak.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	 * 제보 신청 내역 저장
	 * */
	@PostMapping
	public ResponseEntity<?> saveReport(@RequestBody ReportRequest reportDto, Principal principal){
		String userId = principal.getName(); 
		
		Report report = new Report();
		report.setTitle(reportDto.getTitle());
		report.setUserId(userId);
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
	 * 제보 신청 내역(첨부 이미지) 저장 
	 * */
	@PostMapping("/upload/{reportId}")
    public ResponseEntity<?> uploadFiles(@PathVariable Long reportId,
                                         @RequestParam("files") List<MultipartFile> files) {

		reportImageService.save(reportId, files);
		
        return ResponseEntity.ok("Files Uploaded");
    }
	
	/**
	 * 제보 신청 내역 수정
	 * */
	@PutMapping("/{reportId}")
	public ResponseEntity<?> updateReport(@PathVariable Long reportId, @RequestBody ReportRequest reportDto, Principal principal) {
	    // 1. 수정할 대상 조회
	    Report report = service.findById(reportId);
	    if (report == null) {
	        return ResponseEntity.notFound().build(); // 없는 경우 404 반환
	    }

	    // 2. 데이터 수정
	    report.setTitle(reportDto.getTitle());
	    report.setUserId(principal.getName());
	    report.setViolationType(reportDto.getViolationType());
	    report.setVehicleNumber(reportDto.getVehicleNumber());
	    report.setDescription(reportDto.getDescription());
	    report.setAddress(reportDto.getAddress());
	    report.setLatitude(reportDto.getLatitude());
	    report.setLongitude(reportDto.getLongitude());

	    // 3. 저장
	    Report updatedReport = service.save(report);

	    // 4. 결과 반환
	    return ResponseEntity.ok(updatedReport.getReportId());
	}
	
	/**
	 * 제보 신청 내역 삭제
	 * */
	@DeleteMapping("/{reportId}")
	public ResponseEntity<?> deleteReport(@PathVariable Long reportId, Principal principal) {
	    service.deleteReport(reportId, principal.getName());
	    return ResponseEntity.ok("제보가 삭제되었습니다.");
	}
}
