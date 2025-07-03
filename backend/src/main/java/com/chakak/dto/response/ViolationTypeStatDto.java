package com.chakak.dto.response;

// 제보 유형별 분포 통계
public interface ViolationTypeStatDto {
    String getViolationType();
    long getCount();
}