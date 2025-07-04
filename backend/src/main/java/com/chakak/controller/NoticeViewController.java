package com.chakak.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.chakak.domain.Notice;
import com.chakak.service.NoticeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NoticeViewController {

	private final NoticeService service;

	// 공지사항 목록
	@GetMapping("/notice/list")
	public String noticeList(Model model) {
		List<Notice> noticeList = service.findAll();
		model.addAttribute("noticeList", noticeList);
		return "notice/notice-list";
	}

	// 공지사항 상세보기
	@GetMapping("/notice/{noticeId}")
	public String noticeDetail(Model model, @PathVariable Long noticeId) {
		Notice notice = service.findById(noticeId);
		model.addAttribute("notice", notice);
		return "notice/notice-detail";
	}

	// 새 글 작성 폼
	@GetMapping("/notice/new")
	public String createNoticeForm(Model model) {
		model.addAttribute("notice", new Notice()); // 빈 객체 전달
		model.addAttribute("mode", "create"); // 작성 모드
		return "notice/notice-form";
	}

	// 기존 글 수정 폼
	@GetMapping("/notice/{noticeId}/edit")
	public String editNoticeForm(Model model, @PathVariable Long noticeId) {
		Notice notice = service.findById(noticeId);
		model.addAttribute("notice", notice); // 기존 객체 전달
		model.addAttribute("mode", "edit"); // 수정 모드
		return "notice/notice-form";
	}
}
