package com.chakak.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ReportImageService {

	public void save(Long reportId, List<MultipartFile> files);

}
