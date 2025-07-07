package com.chakak.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chakak.domain.Reaction;
import com.chakak.domain.Report;
import com.chakak.domain.User;
import com.chakak.dto.ReportDto;
import com.chakak.repository.ReactionRepository;
import com.chakak.repository.ReportRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;



@Service
public class ReactionService {
	
	@Autowired
	private ReactionRepository reactionRepository;
	
	@Autowired
	private ReportRepository reportRepository;
	
	
	
	public List<ReportDto> getReactionsByType(String userId , String reactionType) {
		List<Reaction> reactions = reactionRepository.findByUser_UserIdAndReactionType(userId, reactionType.toUpperCase());
		return reactions.stream().map(reaction-> ReportDto.fromEntity(reaction.getReport())).collect(Collectors.toList());
	}
	
	
	
	
	@Transactional 
	public void toggleReaction ( Long reportId , User user , String reactionType) {
		// report 조회하기 
		Report report = reportRepository.findById(reportId).orElseThrow(()->new EntityNotFoundException("해당 제보글이 존재하지 않습니다."));
		
		//2. 기존에 등록된 반응 조회 
		List<Reaction> existing = reactionRepository.findByUserAndReport(user ,report);
		
		//3. 동일한 반응이 이미 등록된 경우 -> 삭제 (toggle off)
		for(Reaction reaction : existing) {
			if ( reaction.getReactionType().equalsIgnoreCase(reactionType)) {
				reactionRepository.delete(reaction);
				return;
			}
		}
		
		//4. 등록된 반응이 없거나 다른 반응인 경우 => 새로 등록 (toggle on)
		Reaction newReaction = new Reaction();
		newReaction.setReport(report);
		newReaction.setUser(user);
        newReaction.setReactionType(reactionType.toUpperCase()); // "LIKE" or "DISLIKE"
        reactionRepository.save(newReaction);
	}
	
	public long countReaction(Long reportId, String reactionType) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("해당 제보글이 존재하지 않습니다."));
        return reactionRepository.countByReportAndReactionType(report, reactionType.toUpperCase());
    }
	
	

}
