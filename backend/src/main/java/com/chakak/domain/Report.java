package com.chakak.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.chakak.common.enums.Violation;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "REPORT")
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    public void setUserId(String userId) {
        if (this.user == null) {
            this.user = new User();
        }
        this.user.setUserId(userId);
    }

    public String getUserId() {
        return this.user != null ? this.user.getUserId() : null;
    }

    private String title;
    private String vehicleNumber;

    @Column(insertable = false, updatable = false)
    private LocalDateTime reportTime;

    @Enumerated(EnumType.STRING)
    private Violation violationType;

    private String address; //지도상 주소
    private double latitude; // 위도
    private double longitude; // 경도
    private String locationType; // 지역(시/구/동 단위)
    private String description;

    @Column(name = "view_cnt")
    private Long viewCount = 0L;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReportImage> reportImages = new ArrayList<>();

	@OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Comment> comments = new ArrayList<>();
}
