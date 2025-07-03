package com.chakak.dto.response;

// 반복 제보 위치 통계 (지역별) 인터페이스 기반 Rojection
public interface FrequentAddressDto {
    String getLocation();
    Long getCount();
}