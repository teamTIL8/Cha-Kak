package com.chakak.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ReportRequest {
	
	private Long reportId;
	
	@NotBlank(message = "제목은 필수입니다.")
	private String title;
	private String userId;
	
	@NotBlank(message = "차량 번호는 필수입니다.")
	@Pattern(
	        regexp = "^[0-9가-힣]{2,4}\\s?[0-9]{4}$",
	        message = "차량 번호 형식이 올바르지 않습니다. 예: 12가 3456"
	    )
	private String vehicleNumber;
	private LocalDateTime reportTime;
	
	@NotBlank(message = "위반 유형은 필수입니다.")
	private String violationType;
	
	@NotBlank(message = "주소는 필수입니다.")
	private String address; //지도상 주소
	private double latitude; // 위도
	private double longitude; // 경도
	
	@NotBlank(message = "설명은 필수입니다.")
	private String description;
	private LocalDateTime createdAt;
}
