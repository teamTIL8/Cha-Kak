package com.chakak.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chakak.domain.Report;
import com.chakak.domain.User;
import com.chakak.repository.ReportRepository;
import com.chakak.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; //로그 디버그용


@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{
	
	private final ReportRepository repository;
    private final UserRepository userRepository;
	
	@Override
	public Report save(Report report) {
		//간접세터 setUserId() 사용 
		String userId = report.getUserId();
		User user = userRepository.findByUserId(userId)
	            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자"));

	    report.setUser(user); // 영속 상태로 다시 교체
		return repository.save(report);
	}

	@Override
	public List<Report> findAll() {
		return repository.findAll();
	}

}
