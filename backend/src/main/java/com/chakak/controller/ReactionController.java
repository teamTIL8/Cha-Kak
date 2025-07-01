package com.chakak.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chakak.dto.ReportDto;
import com.chakak.service.ReactionService;



@RestController
@RequestMapping("/api")
public class ReactionController {
	
	@Autowired
	private ReactionService reactionService;
	
	@GetMapping("/users/me/likes") 
	public ResponseEntity<List<ReportDto>> getLikedReports() {
		String userId = "jiwoo03";
		return ResponseEntity.ok(reactionService.getReactionsByType(userId,"LIKE" ));
	}
	
	@GetMapping("/users/me/dislikes")
	public ResponseEntity<List<ReportDto>> getDislikedReprots() {
		String userId = "jiwoo03";
		return ResponseEntity.ok(reactionService.getReactionsByType(userId, "DISLIKE"));
	}

}
