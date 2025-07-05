package com.chakak.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chakak.domain.Report;
import com.chakak.service.AdminUserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor // 생성자 주입 lombok 어노테이션(final, @NonNull이 붙은 필드들만 매개변수로 받는 생성자를 자동 생성)
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    /**
     * 일반 사용자 목록
     */
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", adminUserService.getUserList());
        return "admin/user/list";
    }
    
    /**
     * 특정 사용자 신고글 목록 조회 (관리자 제외)
     */
    @GetMapping("/{userId}/reports")
    public String userReports(@PathVariable String userId, Model model) {
        List<Report> reports = adminUserService.getReportsByUser(userId);
        model.addAttribute("reports", reports);
        model.addAttribute("userId", userId);
        return "admin/user/reports";
    }

    /**
     * 특정 사용자 탈퇴 기능
     */
    @PostMapping("/{userId}/delete")
    public String forceDelete(@PathVariable String userId) {
        adminUserService.forceDeleteUser(userId);
        return "redirect:/admin/users";
    }
    
    
}