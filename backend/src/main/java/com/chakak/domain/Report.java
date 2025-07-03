package com.chakak.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "report")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Report {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reportId;
	
	//연관관계 매핑 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) //user_id컬럼과 DB매핑
    private User user;
	
    // 간접 setter
    public void setUserId(String userId) {
        if (this.user == null) {
            this.user = new User();
        }
        this.user.setUserId(userId); 
    }
    
    // 간접 getter
    public String getUserId() {
        return this.user != null ? this.user.getUserId() : null;
    }
    
	private String title;
	private String vehicleNumber; 

	@Column(insertable = false, updatable = false)
	private LocalDateTime reportTime;
	
	private String violationType;
	private String address; //지도상 주소
	private double latitude; // 위도
	private double longitude; // 경도
	private String description;
	
    @Column(name = "view_cnt")
    private Long viewCount = 0L;
	
	@Column(insertable = false, updatable = false)
	private LocalDateTime createdAt;
}
