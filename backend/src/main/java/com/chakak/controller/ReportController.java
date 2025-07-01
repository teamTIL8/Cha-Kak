package com.chakak.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chakak.dto.ReportDto;
import com.chakak.service.ReportService;

//import com.streetcheck.CustomUserDetails;

//import com.streetcheck.dto.ReportDetailDto;


@RestController
@RequestMapping("/api")
public class ReportController {
	@Autowired
	private ReportService reportService;
	
	// 전체 신고 목록 조회 or 필터링 조회 ( 차량 번호 , 위치 , 상태 , 글 쓴 날짜 , 기간 ( startDate , endDate) , 키워드 )
	@GetMapping("/reports")
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

	    Page<ReportDto> page = reportService.getAllReports(
	        carNumber, location, 
	        reportDate, violationType, startDate, endDate,
	        keyword, pageable
	    );

	    return ResponseEntity.ok(page);
	}
	
	// 내 신고글 조회
	//////////////////// << 이게 원래 코드임 !! 팀원들이랑 병합 시 이걸로 쓸 거임 밑에 건 가짜>>
	/*
	@GetMapping("/users/me/reports")
	public ResponseEntity<List<ReportDto>> getMyReports(@AuthenticationPrincipal CustomUserDetails userDetails ) {
		return ResponseEntity.ok(reportService.getMyReports(user.getUsername()));
	}
	*/
	
	////////////////////////////////////////////////////////////////////////////////
	///
	///<< 가짜 코드임 >>//
	///// postman 에서 인증 처리된 상태로 받기 위한 그냥 메소드일 뿐...
	@GetMapping("/users/me/reports")
	public ResponseEntity<List<ReportDto>> getMyReports() {
	    String userId = "jiwoo03"; // 테스트용
	    return ResponseEntity.ok(reportService.getMyReports(userId));
	}

	///////////////////
	////////////////////
	
	
	// 상세 조회 ( 조회수 증가 ) 
	 @GetMapping("/reports/{id}")
	    public ResponseEntity<ReportDto> getDetail(@PathVariable Long id) {
		    ReportDto report = reportService.getReport(id);
	        return ResponseEntity.ok(report);
	    }

}
