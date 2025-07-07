package com.chakak.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chakak.domain.User;
import com.chakak.dto.ReportDto;
import com.chakak.service.CustomUserDetails;
import com.chakak.service.ReactionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/")
@Slf4j 
public class ReactionController {
	
	@Autowired
	private ReactionService reactionService;
	
	@GetMapping("/users/me/likes") 
	public ResponseEntity<List<ReportDto>> getLikedReports(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
		 if (userDetails == null) {
	            return ResponseEntity.status(401).build(); // 인증 안 된 경우 401 반환
	        }

	        String userId = userDetails.getUsername(); 
        return ResponseEntity.ok(reactionService.getReactionsByType(userId, "LIKE"));
    }
	
	@GetMapping("/users/me/dislikes")
	public ResponseEntity<List<ReportDto>> getDislikedReports(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
		 if (userDetails == null) {
	            return ResponseEntity.status(401).build(); // 인증 안 된 경우 401 반환
	        }

	        String userId = userDetails.getUsername(); 

        return ResponseEntity.ok(reactionService.getReactionsByType(userId, "DISLIKE"));
    }
	

	// 좋아요 또는 싫어요 등록 / 취소 (toggle)
	//지금 밑에 이 부분은 auth 인증 받아서 할 때 사용하는 거임
	 @PostMapping("/reactions/{reportId}")
	 
	 public ResponseEntity<String> toggleReaction(
	            @PathVariable Long reportId,
	            @RequestParam String reactionType,
	            @AuthenticationPrincipal CustomUserDetails userDetails) {
		
		// 다음 두 줄의 로그를 추가합니다.
         log.debug("toggleReaction method: userDetails received: {}", userDetails != null ? userDetails.getUsername() : "null");
         log.debug("toggleReaction method: userDetails type: {}", userDetails != null ? userDetails.getClass().getName() : "null");
		   if (userDetails == null) {
	            return ResponseEntity.status(401).body("인증이 필요합니다.");
	        }

		
	        User user = new User();
	        user.setUserId(userDetails.getUsername());

	        reactionService.toggleReaction(reportId, user, reactionType);
	        return ResponseEntity.ok("반응이 처리되었습니다.");
	    }
	
	
	
	//좋아요 또는 싫어요 개수 
	 @GetMapping("/reactions/{reportId}/count")
	    public ResponseEntity<Long> countReaction(
	            @PathVariable Long reportId,
	            @RequestParam String reactionType
	    ) {
	        long count = reactionService.countReaction(reportId, reactionType);
	        return ResponseEntity.ok(count);
	    }
}
