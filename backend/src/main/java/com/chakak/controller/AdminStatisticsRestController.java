package com.chakak.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chakak.dto.response.ReportCoordinateDto;
import com.chakak.service.AdminStatisticsService;

import lombok.RequiredArgsConstructor;

// json응답하는 restController - Kakao Map JS 스크립트에 연동
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistics")
public class AdminStatisticsRestController {
	
    private final AdminStatisticsService statisticsService;

    // 히트맵/마커용 좌표 리스트
    @GetMapping("/map")
    public List<ReportCoordinateDto> getAllCoordinates() {
    	
        return statisticsService.getAllReportCoordinates();
    }
	
}