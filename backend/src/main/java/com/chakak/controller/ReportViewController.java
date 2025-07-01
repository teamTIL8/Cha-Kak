package com.chakak.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.chakak.common.enums.Violation;
import com.chakak.domain.Report;
import com.chakak.service.ReportService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReportViewController {
	
	private final ReportService service;
	
	/**
	 * 불
	 * */
	@GetMapping("/report/list")
	public String listReports(Model model) {
		List<Report> reportList = service.findAll();
		model.addAttribute("reportList", reportList);
		
		return "report/report-list";
	}
	
	/**
	 * 불법 주정차 신고하기 화면 이동
	 * */
	@GetMapping("/report/new")
	public String createReportForm(Model model) {
		// 위반 구분
		Violation[] violationTypeList = Violation.values();
		
		model.addAttribute("violationTypeList", violationTypeList);
		return "report/report-form";
	}
	
	/**
	 * 불법 주정차 수정하기 화면 이동
	 * */
	@GetMapping("/reports/{id}/edit")
	public String editReportForm(Model model) {
		model.addAttribute("test", "hello thymeleaf");
		return "report/report-form";
	}
	
}
