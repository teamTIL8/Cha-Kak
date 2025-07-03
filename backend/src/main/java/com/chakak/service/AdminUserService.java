package com.chakak.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chakak.common.constants.Role;
import com.chakak.domain.Report;
import com.chakak.domain.User;
import com.chakak.dto.response.UserDto;
import com.chakak.repository.ReportRepository;
import com.chakak.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;

    // 전체 유저 목록 (soft delete 제외)
    public List<UserDto> getUserList() {
        return userRepository.findAllActiveUsersOnly().stream()
            .map(user -> {
                long reportCount = reportRepository.countByUser_UserId(user.getUserId());
                return UserDto.fromEntity(user, reportCount);
            })
            .toList();
    }
    
    // 특정 사용자 신고글 조회 (role 검증 포함)
    public List<Report> getReportsByUser(String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("유저 없음"));

        if (user.getRole() != Role.USER) {
            throw new RuntimeException("일반 사용자만 신고글 이력이 조회됩니다.");
        }

        return reportRepository.findByUser_UserId(userId);
    }
    

    // 강제 탈퇴
    public void forceDeleteUser(String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("유저 없음"));
        user.setIsDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}