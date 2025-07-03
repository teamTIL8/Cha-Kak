package com.chakak.dto.response;

import java.time.LocalDateTime;

import com.chakak.domain.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String userId;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private long reportCount;

    // DTO -> DTO 또는 Entity -> DTO로 직접 수동 매핑
    public static UserDto fromEntity(User user, long reportCount) {
        return UserDto.builder()
            .userId(user.getUserId())
            .name(user.getName())
            .email(user.getEmail())
            .createdAt(user.getCreatedAt())
            .reportCount(reportCount)
            .build();
    }
}