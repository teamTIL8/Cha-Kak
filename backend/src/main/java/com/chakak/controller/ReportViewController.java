package com.chakak.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.chakak.common.enums.Violation;
import com.chakak.domain.Report;
import com.chakak.domain.ReportImage;
import com.chakak.dto.ReportDto;
import com.chakak.dto.response.ReportCommentResponse;
import com.chakak.dto.response.ReportResponse;
import com.chakak.service.ReportCommentService;
import com.chakak.service.ReportService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class ReportViewController {
	
	private final ReportService service;
	private final ReportCommentService commentSerivce;
	
	/**
	 * 불법 주정차 제보 목록 화면 이동
	 * */
	@GetMapping({"/report/list", "/report/all-list"})
	public String listReports(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(required = false) String carNumber,
	        @RequestParam(required = false) String location,
	        @RequestParam(required = false) String reportDate,
	        @RequestParam(required = false) String violationType,
	        @RequestParam(required = false) String startDate,
	        @RequestParam(required = false) String endDate,
	        @RequestParam(required = false) String keyword,
	        Model model) {

	    Pageable pageable = PageRequest.of(page, size);

	    Page<ReportDto> reportPage = service.getAllReports(
	        carNumber, location, reportDate, violationType, startDate, endDate, keyword, pageable
	    );

	    model.addAttribute("reportPage", reportPage);
	    model.addAttribute("reportList", reportPage.getContent());
	    model.addAttribute("currentPage", page);
	    model.addAttribute("carNumber", carNumber);
	    model.addAttribute("location", location);
	    model.addAttribute("reportDate", reportDate);
	    model.addAttribute("violationType", violationType);
	    model.addAttribute("startDate", startDate);
	    model.addAttribute("endDate", endDate);
	    model.addAttribute("keyword", keyword);

	    return "report/report-list";
	}



	
	/**
	 * 불법 주정차 제보 신청 화면 이동
	 * */
	@GetMapping("/report/new")
	public String createReportForm(Model model) {
		// 위반 구분
		Violation[] violationTypeList = Violation.values();
		
		model.addAttribute("violationTypeList", violationTypeList);
		return "report/report-form";
	}
	
	/**
	 * 불법 주정차 제보 수정 화면 이동
	 * */
	@GetMapping("/report/edit/{reportId}")
	public String editReportForm(Model model, @PathVariable Long reportId) {
		
		Report report = service.findById(reportId);
		
		ReportResponse reportResponse = new ReportResponse(report.getReportId(), 
															 report.getTitle(), 
															 report.getUserId(),
															 report.getVehicleNumber(),
															 report.getReportTime(),
															 report.getViolationType(),
															 report.getAddress(),
															 report.getLatitude(),
															 report.getLongitude(),
															 report.getDescription());
		
		
		List<ReportImage> reportImages = report.getReportImages();
		
		model.addAttribute("report", reportResponse);
		model.addAttribute("reportImages", reportImages);
		model.addAttribute("violationTypeList", Violation.values());
		
		return "report/report-edit";
	}
	
	/**
	 * 불법 주정차 제보 상세 화면 이동
	 * */
	@GetMapping("/report/detail/{reportId}")
	public String detailReport(Model model, @PathVariable Long reportId, Principal principal) {
		Report report = service.findById(reportId);
		
		ReportResponse reportResponse = new ReportResponse(report.getReportId(), 
															 report.getTitle(), 
															 report.getUserId(),
															 report.getVehicleNumber(),
															 report.getReportTime(),
															 report.getViolationType(),
															 report.getAddress(),
															 report.getLatitude(),
															 report.getLongitude(),
															 report.getDescription());
		
		
		List<ReportImage> reportImages = report.getReportImages();
		
		// 댓글 조회
		List<ReportCommentResponse> comments = commentSerivce.findByReportId(reportId);
		String currentuserId = principal.getName();
		
		model.addAttribute("report", reportResponse);
		model.addAttribute("reportImages", reportImages);
		model.addAttribute("comments", comments);
		model.addAttribute("currentUserId", principal.getName());
		
		return "report/report-detail";
	}
	
}
