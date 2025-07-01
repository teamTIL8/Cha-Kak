package com.chakak.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chakak.dto.CommentDto;
import com.chakak.service.CommentService;



@RestController
@RequestMapping("/api")
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@GetMapping("/users/me/comments")
	public ResponseEntity<List<CommentDto>> getMyComments() {
		//임시 userId 
		String userId = "jiwoo02";
		return ResponseEntity.ok(commentService.getMyComment(userId));
	}

}
