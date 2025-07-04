package com.chakak.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chakak.common.enums.Violation;
import com.chakak.domain.Report;
import com.chakak.dto.response.ReportCoordinateDto;
import com.chakak.dto.response.ReportResponse;
import com.chakak.service.AdminStatisticsService;

import lombok.RequiredArgsConstructor;

// json응답하는 restController - Kakao Map JS 스크립트에 연동
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/statistics")
@PreAuthorize("hasRole('ADMIN')")
public class AdminStatisticsRestController {
	
    private final AdminStatisticsService statisticsService;

    // 히트맵/마커용 좌표 리스트
    @GetMapping("/map")
    public List<ReportCoordinateDto> getAllCoordinates() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("🔐 AUTH = " + auth);
        System.out.println("🔐 AUTHORITIES = " + auth.getAuthorities());
        return statisticsService.getAllReportCoordinates();
    }
    
    @GetMapping("/by-type")
    public List<ReportResponse> getReportsByViolationType(@RequestParam Violation violationType) {
        return statisticsService.getReportsByViolationType(violationType).stream()
                .map(r -> new ReportResponse(
                        r.getReportId(),
                        r.getTitle(),
                        r.getUserId(),
                        r.getVehicleNumber(),
                        r.getReportTime(),
                        r.getViolationType(),
                        r.getAddress(),
                        r.getLatitude(),
                        r.getLongitude(),
                        r.getDescription()
                ))
                .toList();
    }

}