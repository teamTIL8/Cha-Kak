package com.chakak.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.chakak.domain.Report;
import com.chakak.dto.ReportDto;
import com.chakak.dto.request.ReportRequest;
import com.chakak.service.ReportImageService;
import com.chakak.service.ReportService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {
	
	private final ReportService reportService;
	private final ReportImageService reportImageService;
	
	// ✅ 제보 신청 내역 저장 
	/*@PostMapping
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
		
		Report savedReport = reportService.save(report);
		return ResponseEntity.ok(savedReport.getReportId());
	}*/
	
	/// 임시 테스트용 다시 되돌릴 거임 
	@PostMapping
	public ResponseEntity<?> saveReport(@RequestBody ReportRequest reportDto) {
	    String userId = "test1234";  //🌟🌟 하드코딩된 userId
	    
	    Report report = new Report();
	    report.setTitle(reportDto.getTitle());
	    report.setUserId(userId);
	    report.setViolationType(reportDto.getViolationType());
	    report.setVehicleNumber(reportDto.getVehicleNumber());
	    report.setDescription(reportDto.getDescription());
	    report.setAddress(reportDto.getAddress());
	    report.setLatitude(reportDto.getLatitude());
	    report.setLongitude(reportDto.getLongitude());
	    
	    Report savedReport = reportService.save(report);
	    return ResponseEntity.ok(savedReport.getReportId());
	}
//////////////
	
	// ✅ 제보 신청 내역(첨부 이미지) 저장 
	@PostMapping("/upload/{reportId}")
    public ResponseEntity<?> uploadFiles(@PathVariable Long reportId,
                                         @RequestParam("files") List<MultipartFile> files) {

		reportImageService.save(reportId, files);
		
        return ResponseEntity.ok("Files Uploaded");
    }
	

	
	
	// ✅ 전체 신고 목록 조회 or 필터링 조회 ( 차량 번호 , 위치 , 상태 , 글 쓴 날짜 , 기간 ( startDate , endDate) , 키워드 )
	@GetMapping
	public ResponseEntity<Page<ReportDto>> getAllReports(
			// required = false는 뒤에 쿼리 파라미터가 안 붙이면 전체 목록 조회 , 붙이면 해당 파라미터 조회를 의미하는 것임
	    @RequestParam(required = false) String carNumber, // RequestParam 이므로 ?로 붙이는 쿼리 파라미터임 
	    @RequestParam(required = false) String location,
	    @RequestParam(required = false) String reportDate,
	    @RequestParam(required = false) String violationType,
	    @RequestParam(required = false) String startDate,
	    @RequestParam(required = false) String endDate,
	    @RequestParam(required = false) String keyword,
	    @PageableDefault(size = 10) Pageable pageable) {
		
		String userId = "test1234"; // 🌟🌟 하드코딩함

	    Page<ReportDto> page = reportService.getAllReports(
	        carNumber, location, 
	        reportDate, violationType, startDate, endDate,
	        keyword, pageable
	    );

	    return ResponseEntity.ok(page);
	}
	
	
	// ✅ 내 신고글 목록 조회하기 
	/* @GetMapping("/my")
	    public ResponseEntity<List<ReportDto>> getMyReports(@AuthenticationPrincipal UserDetails userDetails) {
	        String userId = userDetails.getUsername();
	        List<ReportDto> reports = reportService.getMyReports(userId);
	        return ResponseEntity.ok(reports);
	    }*/
	///테스트용 ////////////////////////////////
	@GetMapping("/my")
	public ResponseEntity<List<ReportDto>> getMyReports() {
	    String userId = "test1234";  // 하드코딩 userId
	    List<ReportDto> reports = reportService.getMyReports(userId);
	    return ResponseEntity.ok(reports);
	}
	///////////////////////////////////////////
	
	// ✅ 상세 조회 ( 조회수 증가 ) 
	 @GetMapping("/detail/{id}")
	    public ResponseEntity<ReportDto> getDetail(@PathVariable Long id) {
		    ReportDto report = reportService.getReport(id);
	        return ResponseEntity.ok(report);
	    }


	
	  // ✅ 제보 신청 내역 수정
	
	@PutMapping("/{reportId}")
	public ResponseEntity<?> updateReport(@PathVariable Long reportId, @RequestBody ReportRequest reportDto, Principal principal) {
	    // 1. 수정할 대상 조회
	    Report report = reportService.findById(reportId);
	    if (report == null) {
	        return ResponseEntity.notFound().build(); // 없는 경우 404 반환
	    }

	    // 2. 데이터 수정
	    report.setTitle(reportDto.getTitle());
	    //report.setUserId(principal.getName());
	    report.setUserId("test1234"); // 🌟🌟 하드코딩 
	    report.setViolationType(reportDto.getViolationType());
	    report.setVehicleNumber(reportDto.getVehicleNumber());
	    report.setDescription(reportDto.getDescription());
	    report.setAddress(reportDto.getAddress());
	    report.setLatitude(reportDto.getLatitude());
	    report.setLongitude(reportDto.getLongitude());

	    // 3. 저장
	    Report updatedReport = reportService.save(report);

	    // 4. 결과 반환
	    return ResponseEntity.ok(updatedReport.getReportId());
	}
	
	/**
	 * 제보 신청 내역 삭제
	 * */
	@DeleteMapping("/{reportId}")
	/*
	public ResponseEntity<?> deleteReport(@PathVariable Long reportId, Principal principal) {
		String userId = "test1234"; //🌟🌟 하드코딩함
		reportService.deleteReport(reportId, principal.getName());
	    return ResponseEntity.ok("제보가 삭제되었습니다.");
	}*/
	//🌟🌟🌟🌟🌟 테스트용 
	
	public ResponseEntity<?> deleteReport(@PathVariable Long reportId) {
		String userId = "test1234"; // 테스트용
		reportService.deleteReport(reportId, userId);
		return ResponseEntity.ok("제보가 삭제되었습니다.");
	}
	///////////////////////
	
	
	
	
}

