package com.chakak.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "REPORT_IMAGE")
public class ReportImage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long imgId;
	private String imgPath;
	@Column(insertable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@ManyToOne
    @JoinColumn(name = "REPORT_ID")
    private Report report;
}
