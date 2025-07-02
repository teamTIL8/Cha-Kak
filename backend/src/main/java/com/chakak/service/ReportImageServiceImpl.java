package com.chakak.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chakak.domain.Report;
import com.chakak.domain.ReportImage;
import com.chakak.repository.ReportImageRepository;
import com.chakak.repository.ReportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportImageServiceImpl implements ReportImageService{
	
	private final ReportImageRepository repository;
	private final ReportRepository reportRepository;
	
	@Override
	public void save(Long reportId, List<MultipartFile> files) {
		//final String uploadDir = "C:/upload/";
		
		Report report = reportRepository.findById(reportId).orElseThrow();
		
        String today = LocalDate.now().toString();
        String savePath = today;

        try {
			Files.createDirectories(Paths.get(savePath));
		} catch (IOException e) {
			e.printStackTrace();
		}

        for (MultipartFile file : files) {
            String uuidFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(savePath, uuidFileName);

            try {
				file.transferTo(filePath.toFile());
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

            ReportImage image = new ReportImage();
            image.setImgPath(filePath.toString());
            image.setReport(report);

            repository.save(image);
        }
	}
}
