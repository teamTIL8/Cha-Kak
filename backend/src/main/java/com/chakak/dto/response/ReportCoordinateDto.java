package com.chakak.dto.response;

// 지역별 제보 히트맵 표시용 좌표 데이터
public interface ReportCoordinateDto {
    Double getLatitude();
    Double getLongitude();
    String getAddress();
}