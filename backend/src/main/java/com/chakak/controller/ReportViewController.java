package com.chakak.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.chakak.common.enums.Violation;
import com.chakak.domain.Report;
import com.chakak.domain.ReportImage;
import com.chakak.dto.response.ReportCommentResponse;
import com.chakak.dto.response.ReportResponse;
import com.chakak.service.ReportCommentService;
import com.chakak.service.ReportService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReportViewController {
	
	private final ReportService service;
	private final ReportCommentService commentSerivce;
	
	/**
	 * 불법 주정차 제보 목록 화면 이동
	 * */
	@GetMapping("/report/list")
	public String listReports(Model model) {
		List<Report> reportList = service.findAll();
		
		List<ReportResponse> reportDtoList = new ArrayList<>();
		for(Report r : reportList) {
			reportDtoList.add(new ReportResponse(r.getReportId(), 
												 r.getTitle(), 
												 r.getUserId(),
												 r.getVehicleNumber(),
												 r.getReportTime(),
												 r.getViolationType(),
												 r.getAddress(),
												 r.getLatitude(),
												 r.getLongitude(),
												 r.getDescription()
												 ));
		}
		
		model.addAttribute("reportList", reportDtoList);
		
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
	@GetMapping("/report/{reportId}")
	public String detailReport(Model model, @PathVariable Long reportId) {
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
		
		model.addAttribute("report", reportResponse);
		model.addAttribute("reportImages", reportImages);
		model.addAttribute("comments", comments);
		
		return "report/report-detail";
	}
	
}
