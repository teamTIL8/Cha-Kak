package com.chakak.service;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chakak.domain.Report;
import com.chakak.domain.User;
import com.chakak.repository.ReportRepository;
import com.chakak.repository.UserRepository;
import com.chakak.util.AddressUtils;

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
	    
	    // address를 지역(location_type)으로 파싱 
	    String address = report.getAddress();
	    String locationType = AddressUtils.extractRegion(address);
	    report.setLocationType(locationType);
		return repository.save(report);
	}

	@Override
	public List<Report> findAll() {
		return repository.findAll();
	}

	@Override
	public Report findById(Long reportId) {
		return repository.findById(reportId).orElse(null);
	}

	@Override
	public void deleteReport(Long reportId, String userId) {
		Report report = repository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 제보입니다."));
		
        if (!report.getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인만 삭제할 수 있습니다.");
        }

        // 1. 첨부 파일 삭제
        report.getReportImages().forEach(image -> {
            File file = new File(image.getImgPath());
            if (file.exists()) {
                file.delete();
            }
        });

        // 2. 제보글 및 연관 댓글, 이미지 DB 삭제
        repository.delete(report);
	}
}
