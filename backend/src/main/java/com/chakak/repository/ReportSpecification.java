package com.chakak.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.chakak.domain.Report;



public class ReportSpecification {
	
	// 차량 번호 조회 코드 
	public static Specification<Report> carNumberContains(String carNumber) {
		return ( root, query , cb) -> {
			if(carNumber == null || carNumber.isEmpty()) {
				return null;
			}
			return cb.like(root.get("vehicleNumber"),"%"+ carNumber + "%");
		};
	}
	
	// 위치 조회 코드 
	public static Specification<Report> locationContains(String location) {
		return (root, query , cb) -> {
			if ( location == null || location.isEmpty()) {
				return null;
			}
			return cb.like(root.get("address"),"%"+location+"%");
		};
	}
	

	// 신고글 게시 날짜 조회 코드 
	public static Specification<Report> reportDateEquals(LocalDate reportDate) {
	    return (root, query, cb) -> {
	        if (reportDate == null) return null;

	        return cb.between(
	            root.get("reportTime"),
	            reportDate.atStartOfDay(),                // 00:00:00
	            reportDate.plusDays(1).atStartOfDay()     // 다음 날 00:00:00 (미만)
	        );
	    };
	}
	
	// reportTime을 잘라서 -> startDate와 endDate 추출 
	// 신고가 작성된 날짜가 특정 기간 내에 있는 신고글만 조회하는 기능 (ex) 3월에서 4월 사이의 신고글 모두 조회하기 ) 
	public static Specification<Report> reportDateBetween(LocalDate startDate, LocalDate endDate) {
	    return (root, query, cb) -> {
	        if (startDate == null || endDate == null) return null;

	        return cb.between(
	            root.get("reportTime"),
	            startDate.atStartOfDay(),
	            endDate.plusDays(1).atStartOfDay()
	        );
	    };
	}
	
	// 위반 유형 조회 코드 
	public static Specification<Report> violationTypeEquals(String violationType) {
		return (root , query , cb) -> {
			if(violationType == null || violationType.isEmpty()) {
				return null;
			}
			
			return cb.equal(root.get("violationType"), violationType);
		};
		
	}
	
	// 검색창에 입력한 키워드로 특정 텍스트를 포함하는 검색 조회 코드
	public static Specification<Report> keywordContains(String keyword){
		return ( root , query , cb) -> {
			if ( keyword == null || keyword.isEmpty()) {
				return null;
			}
			
			return cb.or(
					cb.like(root.get("title"),"%" + keyword+"%"),
					cb.like(root.get("vehicleNumber"),"%" + keyword+"%"),
					cb.like(root.get("address") , "%" + keyword+"%"),
					cb.like(root.get("description"), "%" + keyword+"%")
					);
		};
	}
	
	

}
