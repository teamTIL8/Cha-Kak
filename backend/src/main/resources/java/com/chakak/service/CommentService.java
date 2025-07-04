package com.chakak.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chakak.dto.CommentDto;
import com.chakak.repository.CommentRepository;



@Service
public class CommentService {
	
	@Autowired
	private CommentRepository commentRepository;
	
	public List<CommentDto> getMyComment(String userId) {
		return commentRepository.findByUser_UserId(userId)
				.stream()
				.map(CommentDto::fromEntity)
				.collect(Collectors.toList());
	}

}
