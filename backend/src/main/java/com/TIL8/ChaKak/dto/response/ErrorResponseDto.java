package com.TIL8.ChaKak.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponseDto {
    private String message;
    private String code; // 선택 사항: 오류 코드 (예: VALIDATION_ERROR, BUSINESS_ERROR)
    // private Map<String, String> details; // 유효성 검사 오류와 같은 상세 정보를 위한 필드 (선택 사항)

    public ErrorResponseDto(String message) {
        this.message = message;
    }
}
