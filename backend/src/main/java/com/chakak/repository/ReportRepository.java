package com.chakak.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chakak.domain.Report;

public interface ReportRepository extends JpaRepository<Report, Long>{

}
