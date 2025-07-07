package com.chakak.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chakak.domain.Notice;
import com.chakak.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

	private final NoticeRepository noticeRepository;

	@Override
	public void save(Notice notice) {
		noticeRepository.save(notice);
	}

	@Override
	public List<Notice> findAll() {
		return noticeRepository.findAllByOrderByCreatedAtDesc();
	}

	@Override
	public Notice findById(Long noticeId) {
		return noticeRepository.findById(noticeId).orElse(null);
	}

	@Override
	public void delete(Long noticeId) {
		noticeRepository.deleteById(noticeId);
	}

	@Override
	public void incrementViewCount(Long noticeId) {
		noticeRepository.incrementViewCount(noticeId);
		
	}

}
