package com.chakak.service;

import java.util.List;

import com.chakak.domain.Notice;

public interface NoticeService {
	public void save(Notice notice);

	public List<Notice> findAll();

	public Notice findById(Long noticeId);

	public void delete(Long noticeId);

	public void incrementViewCount(Long noticeId);

}
