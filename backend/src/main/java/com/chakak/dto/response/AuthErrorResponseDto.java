package com.chakak.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthErrorResponseDto {
    private String message;
    private String code; // 선택 사항: 오류 코드 (예: VALIDATION_ERROR, BUSINESS_ERROR)

    public AuthErrorResponseDto(String message) {
        this.message = message;
    }
}
