package com.chakak.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chakak.dto.ReportDto; // ReportDto를 사용하도록 변경
import com.chakak.service.CommentService;
import com.chakak.service.ReportService;

@Controller 
@RequestMapping("/report") 
public class WebController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private CommentService commentService; 
    // 루트 페이지 또는 신고 목록 페이지 (Thymeleaf 사용)
    /*@GetMapping({"/all-list"})
    public String getAllReports(
            @RequestParam(required = false) String carNumber,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String reportDate,
            @RequestParam(required = false) String violationType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10) Pageable pageable,
            Model model) {

        Page<ReportDto> reportPage = reportService.getAllReports(
                carNumber, location,
                reportDate, violationType, startDate, endDate,
                keyword, pageable
        );

        model.addAttribute("reportPage", reportPage); // 모델에 페이징된 신고 목록 추가
        model.addAttribute("currentPage", reportPage.getNumber());
        model.addAttribute("totalPages", reportPage.getTotalPages());
        // 검색 조건을 유지하기 위해 모델에 추가
        model.addAttribute("carNumber", carNumber);
        model.addAttribute("location", location);
        model.addAttribute("reportDate", reportDate);
        model.addAttribute("violationType", violationType);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("keyword", keyword);

        return "report/report-list"; 
    }*/


    // 신고 상세 페이지 (Thymeleaf 사용)
    @GetMapping("/view/{reportId}")
    public String getReportDetail(@PathVariable Long reportId, Model model) {
        // ReportDetailDto 대신 ReportDto를 사용하도록 변경
        ReportDto reportDetail = reportService.getReportDetail(reportId); // 상세 정보 가져오기
        model.addAttribute("report", reportDetail);

        return "reports/report-detail"; 
    }
  

    // 마이페이지 - 내가 작성한 신고글 목록 (Thymeleaf 사용)
    @GetMapping("/my/reports")
    public String getMyReports(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String userId = userDetails.getUsername();
        List<ReportDto> myReports = reportService.getMyReports(userId);
        model.addAttribute("myReports", myReports);
        return  "report/my/reports";
    }

  
}