package com.chakak.repository;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.chakak.domain.Report;
import com.chakak.dto.response.FrequentAddressDto;
import com.chakak.dto.response.ReportCoordinateDto;
import com.chakak.dto.response.TopVehicleReportDto;
import com.chakak.dto.response.ViolationTypeStatDto;

public interface ReportRepository extends JpaRepository<Report, Long> {

    // 특정 유저가 작성한 신고글 리스트
    List<Report> findByUser_UserId(String userId);

    // 특정 유저가 작성한 신고글 개수
    long countByUser_UserId(String userId);

    // 키워드로 제목 검색 (선택)
    List<Report> findByTitleContaining(String keyword);
    
    // 중복 제보 차량 top 10
    @Query(value = """
    	    SELECT r.vehicle_number AS vehicleNumber, COUNT(*) AS count
    	    FROM report r
    	    GROUP BY r.vehicle_number
    	    ORDER BY count DESC
    	    LIMIT 10
    	""", nativeQuery = true)
    List<TopVehicleReportDto> findTop10VehicleReports(); // Pageable.ofSize(10) 로 상위 10개 제한
    
    // 제보 유형별 분포 통계
    @Query(value = """
    	    SELECT violation_type AS violationType, COUNT(*) AS count
    	    FROM report
    	    GROUP BY violation_type
    	    ORDER BY count DESC
    	""", nativeQuery = true)
    List<ViolationTypeStatDto> getViolationTypeStats();
    
    // 반복 제보 위치 통계 (지역별)
    @Query(value = """
    	    SELECT location_type AS location, COUNT(*) AS count
    	    FROM report
    	    WHERE location_type IS NOT NULL
    	    GROUP BY location_type
    	    ORDER BY count DESC
    	    LIMIT 10
    	""", nativeQuery = true)
    	List<FrequentAddressDto> getFrequentAddresses();
    
    // 지역별 제보 히트맵 표시용 좌표 데이터
    @Query(value = """
    	    SELECT latitude, longitude
    	    FROM report
    	    WHERE latitude IS NOT NULL AND longitude IS NOT NULL
    	""", nativeQuery = true)
    List<ReportCoordinateDto> getAllReportCoordinates();
}