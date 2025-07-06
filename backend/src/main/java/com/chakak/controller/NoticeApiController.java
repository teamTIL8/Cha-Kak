package com.chakak.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chakak.domain.Notice;
import com.chakak.service.NoticeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/notice")
@PreAuthorize("hasRole('ADMIN')")
public class NoticeApiController {

	private final NoticeService service;

	@PostMapping
	public ResponseEntity<?> saveNotice(@RequestBody Notice notice) {
		notice.setCreated_at(LocalDateTime.now());
		service.save(notice);
		return ResponseEntity.ok("success");
	}

	@PutMapping("/{noticeId}")
	public ResponseEntity<?> updateNotice(@RequestBody Notice notice, @PathVariable Long noticeId) {
		Notice res = service.findById(noticeId);
		res.setTitle(notice.getTitle());
		res.setContent(notice.getContent());
		res.setUpdatedUser(notice.getUpdatedUser());
		res.setUpdated_at(LocalDateTime.now());
		service.save(res);
		return ResponseEntity.ok("success");
	}

	@DeleteMapping("/{noticeId}")
	public ResponseEntity<?> deleteNotice(@PathVariable Long noticeId) {
		service.delete(noticeId);
		return ResponseEntity.ok("deleted");
	}

	@PutMapping("/{noticeId}/view")
	public ResponseEntity<?> increaseViewCount(@PathVariable Long noticeId) {
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		service.incrementViewCount(noticeId);
		return ResponseEntity.ok().build();
	}

}
