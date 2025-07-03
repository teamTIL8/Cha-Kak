package com.chakak.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 중복 제보 차량 순위 top10 통계
public interface TopVehicleReportDto {
    String getVehicleNumber();
    long getCount();
}