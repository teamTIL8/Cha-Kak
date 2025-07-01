package com.chakak.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chakak.domain.Reaction;
import com.chakak.dto.ReportDto;
import com.chakak.repository.ReactionRepository;



@Service
public class ReactionService {
	
	@Autowired
	private ReactionRepository reactionRepository;
	
	public List<ReportDto> getReactionsByType(String userId , String reactionType) {
		List<Reaction> reactions = reactionRepository.findByUser_UserIdAndReactionType(userId, reactionType);
		return reactions.stream().map(reaction-> ReportDto.fromEntity(reaction.getReport())).collect(Collectors.toList());
	}
	
	

}
