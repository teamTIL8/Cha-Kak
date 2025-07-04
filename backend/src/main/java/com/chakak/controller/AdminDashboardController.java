package com.chakak.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chakak.common.enums.Violation;
import com.chakak.service.AdminStatisticsService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminDashboardController {

	private final AdminStatisticsService statisticsService;

	/**
	 * 관리자 메인 대시보드 (중복 차량 Top 10 표시)
	 */
	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		System.out.println("대시보드 접근 성공");
		model.addAttribute("topVehicles", statisticsService.getTopVehicleReports()); // 차량 통계
		model.addAttribute("typeStats", statisticsService.getViolationTypeStats()); // 유형별 통계
		model.addAttribute("locationStats", statisticsService.getFrequentAddresses()); // 반복 지역 통계

		Violation[] violationTypeList = Violation.values();
		model.addAttribute("violationTypeList", violationTypeList);

		return "admin/dashboard";
	}
}