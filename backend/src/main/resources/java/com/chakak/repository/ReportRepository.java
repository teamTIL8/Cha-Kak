package com.chakak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.chakak.domain.Report;



// 정적 쿼리 ( JpaRepository )와 동적 쿼리 사용 ( JpaSpecificationExecutor ) 
@Repository
public interface ReportRepository extends JpaRepository<Report , Long>, JpaSpecificationExecutor<Report>{
	List<Report> findByUser_UserId(String userId);
	
	

}
